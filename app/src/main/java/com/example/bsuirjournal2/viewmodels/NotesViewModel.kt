package com.example.bsuirjournal2.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bsuirjournal2.GroupNumbersApplication
import com.example.bsuirjournal2.data.NotesRepository
import com.example.bsuirjournal2.model.Note
import com.example.bsuirjournal2.model.User
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotesViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    var notes: List<Note> = emptyList()
    val noteToPatch: Note = Note(1998, "user2", "UUUUUFFF")

    fun patchANote(token: String, id: Long, noteToPatch: Note) {
        viewModelScope.launch {
            notesRepository.patchANote(token, id, noteToPatch).enqueue(object : Callback<Note> {
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    Log.d("Notes", "Patched")
                }
                override fun onFailure(call: Call<Note>, t: Throwable) {
                    Log.d("Notes", "not Patched")
                }
            })
        }
    }
    fun deleteANote(token: String, id: Long) {
        viewModelScope.launch {
            notesRepository.deleteANote(token, id).enqueue(object : Callback<Note> {
                override fun onFailure(call: Call<Note>, t: Throwable) {
                    Log.d("Notes", "del failed")
                }
                override fun onResponse(
                    call: Call<Note>,
                    response: Response<Note>
                ) {
                    Log.d("Notes", "del not failed")
                }
            })
        }
    }

    fun getNote(token: String, id: Long) {
        viewModelScope.launch {
            notesRepository.getNote(token, 20).enqueue(object : Callback<Note> {
                override fun onFailure(call: Call<Note>, t: Throwable) {
                    Log.wtf("Notes", "get fail")
                }
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    Log.d("Notes", "got ${response.body()}")
                }
            })
        }
    }

    fun getAllNotes(token: String) {
        viewModelScope.launch {
            notes = notesRepository.getAllNotes(token)
        }
        for (note in notes) {
            Log.wtf("NotesList", "$note")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GroupNumbersApplication)
                val notesRepository = application.container.notesRepository
                NotesViewModel(
                    notesRepository = notesRepository
                )
            }
        }
    }
}