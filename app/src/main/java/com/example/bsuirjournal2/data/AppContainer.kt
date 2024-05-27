/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.bsuirjournal2.data

import com.example.bsuirjournal2.network.AuthorisationApiService
import com.example.bsuirjournal2.network.GroupsApiService
import com.example.bsuirjournal2.network.NotesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val groupNumbersRepository: GroupNumbersRepository
    val notesRepository: NotesRepository
    val authorisationRepository: AuthorisationRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl =
        "https://iis.bsuir.by/api/v1/"
    private val baseUrl2 = "http://192.168.1.33:8080/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
    private val retrofit2: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl2)
        .build()
    //80.249.89.149

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: GroupsApiService by lazy {
        retrofit.create(GroupsApiService::class.java)
    }
    private val retrofitNoteService: NotesApiService by lazy {
        retrofit2.create(NotesApiService::class.java)
    }

    private val retrofitAuthorisationService: AuthorisationApiService by lazy {
        retrofit2.create(AuthorisationApiService::class.java)
    }

    /**
     * DI implementation for Mars photos repository
     */
    override val groupNumbersRepository: GroupNumbersRepository by lazy {
        NetworkGroupNumbersRepository(retrofitService)
    }
    override val notesRepository: NotesRepository by lazy {
        NetworkNotesRepository(retrofitNoteService)
    }
    override val authorisationRepository: AuthorisationRepository by lazy {
        NetworkAuthorisationRepository(retrofitAuthorisationService)
    }
}
