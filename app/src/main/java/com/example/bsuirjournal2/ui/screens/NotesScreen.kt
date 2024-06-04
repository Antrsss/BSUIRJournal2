package com.example.bsuirjournal2.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bsuirjournal2.model.Note
import com.example.bsuirjournal2.ui.theme.primaryLight
import com.example.bsuirjournal2.viewmodels.AuthorisationViewModel
import com.example.bsuirjournal2.viewmodels.NotesViewModel

@Composable
fun NoteScreen(
    notesViewModel: NotesViewModel,
    authorisationViewModel: AuthorisationViewModel,
) {
    val token = authorisationViewModel.token

    Log.d("Notes", "token: Bearer $token")

    notesViewModel.getAllNotes(token)

    val notesList = remember { mutableStateOf(notesViewModel.notes) }
    val showDialog = remember { mutableStateOf(false) }
    val author = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            AddNoteButton(
                onClick = {
                    content.value = ""
                    showDialog.value = true
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Text(
                    text = "List of Notes",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                NotesList(
                    notesList,
                    notesViewModel,
                    token
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )

    if (showDialog.value) {
        NewNoteDialog(
            token = token,
            title = author.value,
            onTitleChange = { author.value = it },
            description = content.value,
            onDescriptionChange = { content.value = it },
            onSaveClick = {
                if (author.value.isNotBlank() && content.value.isNotBlank()) {
                    val newNote = Note(id = 0, author.value, content.value)
                    notesViewModel.patchANote(token = token, noteToPatch = newNote)
                    notesList.value.add(newNote)
                    showDialog.value = false
                }
            },
            onCancelClick = { showDialog.value = false }
        )
    }

}

@Composable
fun NotesList(
    notesList: MutableState<MutableList<Note>>,
    notesViewModel: NotesViewModel,
    token: String?
) {
    if (notesViewModel.notes.isEmpty()) {
        Text(
            text = "Empty",
            fontSize = 16.sp,
            modifier = Modifier.fillMaxSize()
        )
    }
    LazyColumn {
        items(notesList.value) { note ->
            Card() {
                Text(text = "Content: ${note.content}")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Button(onClick = { notesViewModel.deleteANote(token, note.id) }) {
                    Text(
                        text = "Delete"
                    )
                }
            }
        }
    }
}
@Composable
fun NewNoteDialog(
    token: String?,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        title = { Text(
            text = "New Note",
            textAlign = TextAlign.Center
        ) },
        text = {
            Column {
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text(text = "Text") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSaveClick) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text(text = "Cancel")
            }
        }
    )
}


@Composable
fun AddNoteButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        backgroundColor = primaryLight
    ) {
        Icon(Icons.Filled.Add, null, tint = Color.White)
    }
}