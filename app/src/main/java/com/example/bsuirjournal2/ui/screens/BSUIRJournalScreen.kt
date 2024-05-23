package com.example.bsuirjournal2.ui.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bsuirjournal2.R
import com.example.bsuirjournal2.roomdatabase.State
import com.example.bsuirjournal2.roomdatabase.SubjectStateEvent
import com.example.bsuirjournal2.viewmodels.GroupsViewModel
import com.example.bsuirjournal2.viewmodels.ScheduleViewModel


enum class BSUIRJournalScreen() {
    Authorisation,
    GroupList,
    Schedule,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BSUIRJournalAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun BSUIRJournalApp (
    sharedPreferences: SharedPreferences,
    state: State,
    onEvent: (SubjectStateEvent) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            BSUIRJournalAppBar(
                canNavigateBack = false,
                navigateUp = { /* TODO: implement back navigation */ }
            )
        }
    ) { innerPadding ->

        val editor = sharedPreferences.edit()

        NavHost(
            navController = navController,
            startDestination = BSUIRJournalScreen.Authorisation.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = BSUIRJournalScreen.Authorisation.name) {
                val groupsViewModel: GroupsViewModel =
                    viewModel(factory = GroupsViewModel.Factory)

                AuthorisationScreen(
                    groupsViewModel = groupsViewModel,
                    navController = navController,
                    sharedPreferences = sharedPreferences,
                    editor = editor,
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = BSUIRJournalScreen.GroupList.name) {
                val groupsViewModel: GroupsViewModel =
                    viewModel(factory = GroupsViewModel.Factory)

                HomeScreen(
                    navController = navController,
                    groupsUiState = groupsViewModel.groupsUiState,
                    sharedPreferences = sharedPreferences,
                    retryAction = { groupsViewModel::getGroupNumbers },
                )
            }
            composable(route = BSUIRJournalScreen.Schedule.name) {
                val scheduleViewModel: ScheduleViewModel =
                    viewModel(factory = ScheduleViewModel.Factory)

                val currentGroup = sharedPreferences.getString("selectedGroup", "")

                val context = LocalContext.current
                MainScreen(
                    scheduleUiState = scheduleViewModel.scheduleUiState,
                    retryAction = { scheduleViewModel::getGroupSchedule },
                    state = state,
                    onEvent = onEvent ,
                    sharedPreferences = sharedPreferences,
                    modifier = Modifier
                )
            }
        }
    }
}
