package com.example.bsuirjournal2.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.bsuirjournal2.viewmodels.NotesViewModel

@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel,
) {
    val showAddNoteScreen = remember{ mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            showAddNoteScreen.value = true
        }
    ) {
        innerPadding ->

        if (showAddNoteScreen.value == true) {
            AddNoteScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }

    }
}

@Composable
fun AddNoteScreen(
    modifier: Modifier
) {

}