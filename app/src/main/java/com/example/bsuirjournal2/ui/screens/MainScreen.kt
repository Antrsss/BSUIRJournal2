package com.example.bsuirjournal2.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bsuirjournal2.data.ScheduleHolder
import com.example.bsuirjournal2.ui.JournalViewModel

@Composable
fun MainScreen(
    viewModel: JournalViewModel,
    scheduleUiState: ScheduleUiState,
    selectedGroup: String,
    currentWeek: Int,
    retryAction: () -> Unit,
    modifier: Modifier
) {
    when (scheduleUiState) {
        is ScheduleUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is ScheduleUiState.Success -> {
            //ScheduleHolder.currentWeekSchedule = scheduleUiState.schedule.schedules
            //
            /*ScheduleScreen(listOfSubjects = )*/
        }
        is ScheduleUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun ScheduleScreen(
    listOfSubjects: List<String>,
) {
    LazyColumn {
        items(listOfSubjects) { item ->
            Text(text = item)
        }
    }
}


