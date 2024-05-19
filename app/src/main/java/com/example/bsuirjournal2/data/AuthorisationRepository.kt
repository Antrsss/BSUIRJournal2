package com.example.bsuirjournal2.data

import com.example.bsuirjournal2.model.AuthorisationResponse
import com.example.bsuirjournal2.model.User
import com.example.bsuirjournal2.network.AuthorisationApiService
import okhttp3.ResponseBody
import retrofit2.Call

interface AuthorisationRepository {
    fun authoriseUser(user: User): Call<AuthorisationResponse>
    fun registerUser(user: User): Call<ResponseBody>
}

class NetworkAuthorisationRepository(
    private val authorisationApiService: AuthorisationApiService
) : AuthorisationRepository {
    override fun authoriseUser(user: User): Call<AuthorisationResponse> =
        authorisationApiService.authorise(user)

    override fun registerUser(user: User): Call<ResponseBody> =
        authorisationApiService.register(user)
}