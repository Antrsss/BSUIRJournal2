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
package com.example.bsuirjournal2.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bsuirjournal2.GroupNumbersApplication
import com.example.bsuirjournal2.data.AuthorisationRepository
import com.example.bsuirjournal2.data.GroupNumbersRepository
import com.example.bsuirjournal2.data.NotesRepository
import com.example.bsuirjournal2.model.Group
import com.example.bsuirjournal2.model.User
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface GroupsUiState {
    data class Success(
        val groups: List<Group>,
        val currentWeek: Int
    ) : GroupsUiState
    object Error : GroupsUiState
    object Loading : GroupsUiState
}

class GroupsViewModel(
    private val groupNumbersRepository: GroupNumbersRepository,
    private val notesRepository: NotesRepository,
    private val authorisationRepository: AuthorisationRepository
) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var groupsUiState: GroupsUiState by mutableStateOf(GroupsUiState.Loading)
        private set

    /*var authorisationUiState: AuthorisationUiState by mutableStateOf(AuthorisationUiState.Error)
        private set*/
    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getGroupNumbers()
    }

    fun registerUser(user: User) {
        viewModelScope.launch {
            authorisationRepository.registerUser(user = user)
        }
    }
    fun authoriseUser(user: User) {
        viewModelScope.launch {
            authorisationRepository.authoriseUser(user = user)
        }
    }
    fun getGroupNumbers() {
        viewModelScope.launch {
            groupsUiState = GroupsUiState.Loading
            groupsUiState = try {
                GroupsUiState.Success(
                    groupNumbersRepository.getGroupNumbers(),
                    groupNumbersRepository.getCurrentWeek()
                )
            } catch (e: IOException) {
                GroupsUiState.Error
            } catch (e: HttpException) {
                GroupsUiState.Error
            }
        }
    }

    /**
     * Factory for [GroupsViewModel] that takes [GroupNumbersRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GroupNumbersApplication)
                val groupNumbersRepository = application.container.groupNumbersRepository
                val notesRepository = application.container.notesRepository
                val authorisationRepository = application.container.authorisationRepository
                GroupsViewModel(
                    groupNumbersRepository = groupNumbersRepository,
                    notesRepository = notesRepository,
                    authorisationRepository = authorisationRepository
                )
            }
        }
    }
}
