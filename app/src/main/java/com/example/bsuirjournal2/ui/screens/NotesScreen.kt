package com.example.bsuirjournal2.ui.screens

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bsuirjournal2.model.Note
import com.example.bsuirjournal2.ui.theme.onPrimaryContainerLight
import com.example.bsuirjournal2.ui.theme.primaryLight
import com.example.bsuirjournal2.viewmodels.AuthorisationViewModel
import com.example.bsuirjournal2.viewmodels.NotesViewModel

@Composable
fun NoteScreen(
    notesViewModel: NotesViewModel,
    authorisationViewModel: AuthorisationViewModel,
    modifier: Modifier
) {
    val token = authorisationViewModel.token

    if (token != null) {
        notesViewModel.getAllNotes(token)

        val notesList = remember { mutableStateOf(notesViewModel.notes) }
        val showNewNoteDialog = remember { mutableStateOf(false) }
        val author = authorisationViewModel.username
        val content = remember { mutableStateOf("") }
        var editingId: MutableState<Long> = remember { mutableStateOf(0) }

        Scaffold(
            floatingActionButton = {
                AddNoteButton(
                    onClick = {
                        content.value = ""
                        showNewNoteDialog.value = true
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Мои заметки:",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NotesList(
                        notesList = notesList,
                        editingId = editingId,
                        onEditButtonClicked = {},
                        notesViewModel = notesViewModel,
                        token = token,
                        modifier = Modifier.fillMaxSize()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        )

        if (showNewNoteDialog.value) {
            NewNoteDialog(
                token = token,
                description = content.value,
                onDescriptionChange = { content.value = it },
                onSaveClick = {
                    if (content.value.isNotBlank()) {
                        val newNote = Note(id = 0, author = author, content = content.value)
                        notesViewModel.postANote(token = token, noteToPost = newNote)
                        notesList.value.add(newNote)
                        showNewNoteDialog.value = false
                    }
                },
                onCancelClick = { showNewNoteDialog.value = false }
            )
        }

        if (editingId.value > 0) {
            EditNoteDialog(
                token = token,
                description = content.value,
                onDescriptionChange = { content.value = it },
                onSaveClick = {
                    if (content.value.isNotBlank()) {
                        val editedNote = Note(id = editingId.value, author = author, content = content.value)
                        notesViewModel.patchANote(token = token, id = editingId.value, noteToPatch = editedNote)
                        for (note in notesList.value) {
                            if (note.id == editingId.value)
                                notesList.value.remove(note)
                        }
                        notesList.value.add(editedNote)
                        showNewNoteDialog.value = false
                    }
                },
                onCancelClick = { editingId.value = 0 }
            )
        }
    }
    else {
        ErrorScreen(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun NotesList(
    notesList: MutableState<MutableList<Note>>,
    notesViewModel: NotesViewModel,
    onEditButtonClicked: (String) -> Unit,
    editingId: MutableState<Long>,
    token: String?,
    modifier: Modifier
) {
    if (notesViewModel.notes.isEmpty()) {
        Text(
            text = "Здесь пусто",
            fontSize = 16.sp,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
    LazyColumn {
        items(notesList.value) { note ->
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .width(200.dp)
                    .padding(vertical = 5.dp, horizontal = 5.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(onPrimaryContainerLight),
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(text = "Содержание:")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "${note.content}")
                    }

                    Column(modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ){
                        Button( onClick = { editingId.value = note.id }
                        ) {
                            Text(
                                text = "Изменить"
                            )
                        }
                        Button(
                            onClick = { notesViewModel.deleteANote(token, note.id) },
                            modifier = Modifier.width(115.dp)
                        ) {
                            Text(
                                text = "Удалить"
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun NewNoteDialog(
    token: String?,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        title = { Text(
            text = "Новая заметка",
            textAlign = TextAlign.Center
        ) },
        text = {
            Column {
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text(text = "Текст...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSaveClick) {
                Text(text = "Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text(text = "Отмена")
            }
        }
    )
}

@Composable
fun EditNoteDialog(
    token: String?,
    onDescriptionChange: (String) -> Unit,
    description: String,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        title = { Text(
            text = "Новая заметка",
            textAlign = TextAlign.Center
        ) },
        text = {
            Column {
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text(text = "Новый текст...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSaveClick) {
                Text(text = "Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text(text = "Отмена")
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