package com.example.bsuirjournal2.network

import com.example.bsuirjournal2.model.AuthorisationResponse
import com.example.bsuirjournal2.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorisationApiService {
    @POST("register")
    fun register(@Body user: User): Call<ResponseBody>

    @POST("authorise")
    fun authorise(@Body user: User): Call<AuthorisationResponse>
}