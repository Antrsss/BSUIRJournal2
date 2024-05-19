package com.example.bsuirjournal2.data

import com.example.bsuirjournal2.model.Note
import com.example.bsuirjournal2.network.NotesApiService
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call

interface NotesRepository {
    suspend fun getAllNotes(token: String): List<Note>
    fun postANote(token: String, note: Note): Call<Note>
    fun deleteANote(token: String, id: Long): Call<Note>
    fun patchANote(token: String, id: Long, note: Note): Call<Note>
    fun getNote(token: String, id: Long): Call<Note>
}

class NetworkNotesRepository(
    private val notesApiService: NotesApiService
) : NotesRepository {
    override suspend fun getAllNotes(token: String): List<Note> = notesApiService.getNotes(token)
    override fun postANote(token: String, note: Note): Call<Note> =
        notesApiService.postNote(token, note)

    override fun deleteANote(token: String, id: Long): Call<Note> =
        notesApiService.deleteNote(token, id)

    override fun patchANote(token: String, id: Long, note: Note): Call<Note> =
        notesApiService.patchNote(token, id, note)

    override fun getNote(token: String, id: Long): Call<Note> = notesApiService.getNote(token, id)
}