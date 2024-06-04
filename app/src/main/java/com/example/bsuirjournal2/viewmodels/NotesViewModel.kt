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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotesViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    val mynote: Note = Note(100, "user2", "from kocklin new")


    val noteToPatch: Note = Note(1998, "user2", "UUUUUFFF")

    var notes = mutableListOf<Note>()


    fun patchANote(token: String?, noteToPatch: Note) {

        notesRepository.patchANote("Bearer $token", 0, noteToPatch).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                Log.d("FUCK YEAH", "Patched")
            }
            override fun onFailure(call: Call<Note>, t: Throwable) {
                Log.d("FUCK NO", "not Patched")
            }
        })
    }

    fun deleteANote(token: String?, id: Long) {

        notesRepository.deleteANote("Bearer $token", id).enqueue(object : Callback<Note> {
            override fun onFailure(call: Call<Note>, t: Throwable) {
                Log.d("del", "del failed")
            }
            override fun onResponse(
                call: Call<Note>,
                response: Response<Note>
            ) {
                Log.d("del", "del not failed")
            }
        })
    }

    fun getNote(token: String?, id: Long) {

        notesRepository.getNote("Bearer $token", id).enqueue(object : Callback<Note> {
            override fun onFailure(call: Call<Note>, t: Throwable) {
                Log.wtf("justget", "fail")
            }
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                Log.d("justget", "got ${response.body()}")
            }
        })
    }

    fun getAllNotes(token: String?) {
        //или вместо viewModelScope попробовать CoroutineScope(Dispatchers.IO)
        viewModelScope.launch {
            val myNotes = notesRepository.getAllNotes("Bearer $token").toMutableList()
            notes = myNotes.toMutableList()
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