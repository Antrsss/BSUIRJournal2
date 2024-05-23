package com.example.bsuirjournal2.viewmodels

import android.util.Log
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
import com.example.bsuirjournal2.data.GroupApiHolder
import com.example.bsuirjournal2.data.GroupNumbersRepository
import com.example.bsuirjournal2.model.Schedule
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ScheduleUiState {
    data class Success(val schedule: Schedule) : ScheduleUiState
    object Error : ScheduleUiState
    object Loading : ScheduleUiState
}

class ScheduleViewModel(
    val groupNumber: String?,
    val groupNumbersRepository: GroupNumbersRepository
) : ViewModel() {
    var scheduleUiState: ScheduleUiState by mutableStateOf(ScheduleUiState.Loading)
        private set
    init {
        getGroupSchedule()
    }
    fun getGroupSchedule() {
        viewModelScope.launch {
            scheduleUiState = ScheduleUiState.Loading
            try {
                val groupSchedule = groupNumbersRepository.getGroupSchedule(groupNumber)
                scheduleUiState = ScheduleUiState.Success(groupSchedule)
                Log.d("ScheduleViewModel", "Schedule loaded successfully: $groupSchedule")
            } catch (e: IOException) {
                scheduleUiState = ScheduleUiState.Error
                Log.e("ScheduleViewModel", "Network error", e)
            } catch (e: HttpException) {
                scheduleUiState = ScheduleUiState.Error
                Log.e("ScheduleViewModel", "HTTP error", e)
            } catch (e: Exception) {
                scheduleUiState = ScheduleUiState.Error
                Log.e("ScheduleViewModel", "Unexpected error", e)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GroupNumbersApplication)
                val groupNumbersRepository = application.container.groupNumbersRepository
                ScheduleViewModel(groupNumber = GroupApiHolder.currentGroup, groupNumbersRepository = groupNumbersRepository)
            }
        }
    }
}
