package com.example.bsuirjournal2.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bsuirjournal2.GroupNumbersApplication
import com.example.bsuirjournal2.data.AuthorisationRepository
import com.example.bsuirjournal2.model.AuthorisationResponse
import com.example.bsuirjournal2.model.User
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface AuthorisationUiState {
    data class Authorised(
        val authorisedFirst: Boolean,
        val tokenFirst: String?,
        val usernameFirst: String?
    ) : AuthorisationUiState
    object Loading : AuthorisationUiState
    object Unauthorised : AuthorisationUiState
    object Registered : AuthorisationUiState
    object FailToRegister : AuthorisationUiState
    object FailToAuthorise : AuthorisationUiState
    object Error : AuthorisationUiState
}
class AuthorisationViewModel(
    private val authorisationRepository: AuthorisationRepository
) : ViewModel() {

    var authorisationUiState: AuthorisationUiState by mutableStateOf(AuthorisationUiState.Unauthorised)
        public set
    var authorised = false
    var username: String? = null
    var password: String? = null
    var token: String? = null


    fun registerUser(user: User) {
        authorisationUiState = AuthorisationUiState.Loading
        try {
            authorisationRepository.registerUser(user).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    authorisationUiState = AuthorisationUiState.FailToRegister
                    Log.wtf("registration", "failed to register")
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        authorisationUiState = AuthorisationUiState.Registered
                    } else {
                        authorisationUiState = AuthorisationUiState.FailToRegister
                        Log.wtf("registration", "failed to register: ${response.code()}")
                    }
                }
            })
        } catch (e: IOException) {
            AuthorisationUiState.Error
        } catch (e: HttpException) {
            AuthorisationUiState.Error
        }
    }

    fun authoriseUser(user: User) {
        authorisationRepository.authoriseUser(user).enqueue(object : Callback<AuthorisationResponse> {
            override fun onFailure(call: Call<AuthorisationResponse>, t: Throwable) {
                Log.wtf("authorisation", "failed to authorise")
                authorisationUiState = AuthorisationUiState.FailToAuthorise
            }

            override fun onResponse(
                call: Call<AuthorisationResponse>,
                response: Response<AuthorisationResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { authorisationResponse ->
                        authorisationUiState =
                            AuthorisationUiState.Authorised(
                                authorisedFirst = true,
                                tokenFirst = authorisationResponse.token,
                                usernameFirst = user.username
                            )
                        Log.d("authorisation", "authorised successfully with token: $token")
                    } ?:
                    run {
                        Log.wtf("authorisation", "response body is null")
                        authorisationUiState = AuthorisationUiState.FailToAuthorise
                    }
                } else {
                    Log.wtf("authorisation", "failed to authorise: ${response.code()}")
                    authorisationUiState = AuthorisationUiState.FailToAuthorise
                }
            }
        })
    }

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