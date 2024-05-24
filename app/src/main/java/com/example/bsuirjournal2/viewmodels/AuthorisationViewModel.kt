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
import com.example.bsuirjournal2.data.AuthorisationRepository
import com.example.bsuirjournal2.data.GroupNumbersRepository
import com.example.bsuirjournal2.data.NotesRepository
import com.example.bsuirjournal2.model.AuthorisationResponse
import com.example.bsuirjournal2.model.User
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AuthorisationViewModel(
    private val authorisationRepository: AuthorisationRepository
) : ViewModel() {

    var registrated = false
    var authorised = false
    var token: String = ""

    fun registerUser(user: User) {
        viewModelScope.launch {
            authorisationRepository.registerUser(user).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.wtf("registration", "failed to register")
                    registrated = false
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            val jsonResponse = responseBody.string()
                            val registrationResponse = Gson().fromJson(jsonResponse, AuthorisationResponse::class.java)
                            token = registrationResponse.token
                            registrated = true
                            Log.d("registration", "registered successfully with token: $token")
                        } ?: run {
                            registrated = false
                            Log.wtf("registration", "response body is null")
                        }
                    } else {
                        registrated = false
                        Log.wtf("registration", "failed to register: ${response.code()}")
                    }
                }
            })
        }
    }

    fun authoriseUser(user: User) {
        viewModelScope.launch {
            authorisationRepository.authoriseUser(user).enqueue(object : Callback<AuthorisationResponse> {
                override fun onFailure(call: Call<AuthorisationResponse>, t: Throwable) {
                    Log.wtf("authorisation", "failed to authorise")
                    authorised = false
                }

                override fun onResponse(
                    call: Call<AuthorisationResponse>,
                    response: Response<AuthorisationResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { authorisationResponse ->
                            token = authorisationResponse.token
                            authorised = true
                            Log.d("authorisation", "authorised successfully with token: $token")
                        } ?: run {
                            authorised = false
                            Log.wtf("authorisation", "response body is null")
                        }
                    } else {
                        authorised = false
                        Log.wtf("authorisation", "failed to authorise: ${response.code()}")
                    }
                }
            })
        }
    }

    /*val user: User = User(id = 2, username = "kotlin", password = "kotlin")
            authorisationRepository.registerUser(user).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.wtf("registration", "failed to register")
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("registration", "registered successfully")
                }
            })
             val mynote: Note = Note(100, "user2", "from kocklin new")
            notesRepository.postANote(token, mynote).enqueue(object : Callback<Note> {
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    Log.d("FUCK YEAH", "Posted")
                }
                override fun onFailure(call: Call<Note>, t: Throwable) {
                    Log.d("FUCK NO", "not Posted")
                }
            })
            notesRepository.deleteANote(token, 14).enqueue(object : Callback<Note> {
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
            val noteToPatch: Note = Note(1998, "user2", "UUUUUFFF")
            notesRepository.patchANote(token, 15, noteToPatch).enqueue(object : Callback<Note> {
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    Log.d("FUCK YEAH", "Patched")
                }
                override fun onFailure(call: Call<Note>, t: Throwable) {
                    Log.d("FUCK NO", "not Patched")
                }
            })
            val notes = notesRepository.getAllNotes(token)
            for (note in notes) {
                Log.wtf("note", "$note")
            }
            notesRepository.getNote(token, 20).enqueue(object : Callback<Note> {
                override fun onFailure(call: Call<Note>, t: Throwable) {
                    Log.wtf("justget", "fail")
                }
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    Log.d("justget", "got ${response.body()}")
                }
            })*/
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GroupNumbersApplication)
                val authorisationRepository = application.container.authorisationRepository
                AuthorisationViewModel(
                    authorisationRepository = authorisationRepository
                )
            }
        }
    }
}