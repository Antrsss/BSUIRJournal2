package com.example.bsuirjournal2.network

import com.example.bsuirjournal2.model.Note
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH

interface NotesApiService {
    @GET("notes/")
    suspend fun getNotes(@Header("Authorization") token: String): List<Note>

    @POST("notes/")
    fun postNote(@Header("Authorization") token: String, @Body note: Note): Call<Note>

    @DELETE("notes/{id}")
    fun deleteNote(@Header("Authorization") token: String, @Path("id") id: Long): Call<Note>

    @PATCH("notes/{id}")
    fun patchNote(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body note: Note
    ): Call<Note>

    @GET("notes/{id}")
    fun getNote(@Header("Authorization") token: String, @Path("id") id: Long): Call<Note>
}