package com.example.bsuirjournal2.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bsuirjournal2.GroupNumbersApplication
import com.example.bsuirjournal2.data.NotesRepository
import com.example.bsuirjournal2.model.Group
import com.example.bsuirjournal2.model.Note
import com.example.bsuirjournal2.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


sealed interface NotesUiState {
    data class Success(
        val notes: List<Note>,
    ) : NotesUiState
    object Error : NotesUiState
    object Loading : NotesUiState
}
class NotesViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    var notesUiState: NotesUiState by mutableStateOf(NotesUiState.Loading)
        private set

    fun postANote(token: String?, noteToPost: Note) {
        notesRepository.postANote("Bearer $token", noteToPost).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                viewModelScope.launch {
                    notesUiState = try {
                        NotesUiState.Success(
                            notesRepository.getAllNotes("Bearer $token")
                        )
                    } catch (e: IOException) {
                        NotesUiState.Error
                    } catch (e: HttpException) {
                        NotesUiState.Error
                    }
                }
                Log.d("FUCK YEAH", "Posted")
            }
            override fun onFailure(call: Call<Note>, t: Throwable) {
                notesUiState = NotesUiState.Error
                Log.d("FUCK NO", "not Posted")
            }
        })
    }
    fun patchANote(token: String?, id: Long, noteToPatch: Note) {

        notesRepository.patchANote("Bearer $token", id, noteToPatch).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                viewModelScope.launch {
                    notesUiState = try {
                        NotesUiState.Success(
                            notesRepository.getAllNotes("Bearer $token")
                        )
                    } catch (e: IOException) {
                        NotesUiState.Error
                    } catch (e: HttpException) {
                        NotesUiState.Error
                    }
                }
                Log.d("FUCK YEAH", "Patched")
            }
            override fun onFailure(call: Call<Note>, t: Throwable) {
                notesUiState = NotesUiState.Error
                Log.d("FUCK NO", "not Patched")
            }
        })
    }

    fun deleteANote(token: String?, id: Long) {

        notesRepository.deleteANote("Bearer $token", id).enqueue(object : Callback<Note> {
            override fun onFailure(call: Call<Note>, t: Throwable) {
                notesUiState = NotesUiState.Error
                Log.d("del", "del failed")
            }
            override fun onResponse(
                call: Call<Note>,
                response: Response<Note>
            ) {
                viewModelScope.launch {
                    notesUiState = try {
                        NotesUiState.Success(
                            notesRepository.getAllNotes("Bearer $token")
                        )
                    } catch (e: IOException) {
                        NotesUiState.Error
                    } catch (e: HttpException) {
                        NotesUiState.Error
                    }
                }
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
        viewModelScope.launch {
            notesUiState = NotesUiState.Loading
            notesUiState = try {
                NotesUiState.Success(
                    notesRepository.getAllNotes("Bearer $token")
                )
            } catch (e: IOException) {
                NotesUiState.Error
            } catch (e: HttpException) {
                NotesUiState.Error
            }
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