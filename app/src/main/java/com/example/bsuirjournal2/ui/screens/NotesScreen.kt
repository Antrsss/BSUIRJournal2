package com.example.bsuirjournal2.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bsuirjournal2.R
import com.example.bsuirjournal2.model.Note
import com.example.bsuirjournal2.viewmodels.AuthorisationViewModel
import com.example.bsuirjournal2.viewmodels.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text("My Notes") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    )
}
@Composable
fun NoteScreen(
    notesViewModel: NotesViewModel,
    authorisationViewModel: AuthorisationViewModel,
) {
    val token = authorisationViewModel.token
    Log.d("Notes", "token: Bearer $token")
    //тут ошибка
    notesViewModel.getAllNotes("Bearer $token")

    val notesList = remember { mutableStateOf(notesViewModel.notes) }
    val showDialog = remember { mutableStateOf(false) }
    val author = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }
    Log.d("Notes", "End")
    Scaffold(
        topBar = { NotesBar() },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Text(text = "List of Notes")
                Spacer(modifier = Modifier.height(16.dp))
                NotesList(notesList)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        author.value = ""
                        content.value = ""
                        showDialog.value = true
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "+ Add New ")
                    Text(text = "New Note")
                }
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
                    notesViewModel.patchANote(id = 0, token = token, noteToPatch = newNote)
                    notesList.value.add(newNote)
                    showDialog.value = false
                }
            },
            onCancelClick = { showDialog.value = false }
        )
    }

}

@Composable
fun NotesList(notesList: MutableState<MutableList<Note>>) {
    LazyColumn {
        items(notesList.value) { note ->
            Text(text = "Author: ${note.author}")
            Text(text = "Content: ${note.content}")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
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
        title = { Text(text = "New Note") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text(text = "Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text(text = "Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSaveClick) {
                Text(text = "Save")
                Icon(Icons.Default.Add, contentDescription = "SAVE")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text(text = "Cancel")
            }
        }
    )
}