package com.example.bsuirjournal2.ui.screens

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bsuirjournal2.R
import com.example.bsuirjournal2.data.GroupApiHolder
import com.example.bsuirjournal2.model.BottomNavItem
import com.example.bsuirjournal2.roomdatabase.State
import com.example.bsuirjournal2.roomdatabase.SubjectStateEvent
import com.example.bsuirjournal2.viewmodels.AuthorisationViewModel
import com.example.bsuirjournal2.viewmodels.GroupsViewModel
import com.example.bsuirjournal2.viewmodels.NotesViewModel
import com.example.bsuirjournal2.viewmodels.ScheduleViewModel

import androidx.compose.material.*
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.navigation.compose.rememberNavController
import com.example.bsuirjournal2.ui.theme.BSUIRJournal2Theme
import com.example.bsuirjournal2.ui.theme.primaryContainerLight

enum class BSUIRJournalScreen() {
    Authorisation,
    GroupList,
    Schedule,
    Notes,
    Account
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = primaryContainerLight,
        elevation = 4.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(imageVector = item.icon, contentDescription = item.name, tint = Color.White)
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BSUIRJournalAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name), color = Color.White) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = primaryContainerLight
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
fun Navigation (
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
        },
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "Аккаунт",
                        route = BSUIRJournalScreen.Account.name,
                        icon = Icons.Default.Person
                    ),
                    BottomNavItem(
                        name = "Расписание",
                        route = BSUIRJournalScreen.Schedule.name,
                        icon = Icons.Default.DateRange
                    ),
                    BottomNavItem(
                        name = "Заметки",
                        route = BSUIRJournalScreen.Notes.name,
                        icon = Icons.Default.Menu
                    )
                ),
                navController = navController,
                modifier = Modifier,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        },
        content = {

            innerPadding ->

            val editor = sharedPreferences.edit()

            val authorisationViewModel: AuthorisationViewModel =
                viewModel(factory = AuthorisationViewModel.Factory)

            val groupsViewModel: GroupsViewModel =
                viewModel(factory = GroupsViewModel.Factory)

            val notesViewModel: NotesViewModel =
                viewModel(factory = NotesViewModel.Factory)

            GroupApiHolder.lastWeek = sharedPreferences.getInt("lastWeek", 0)

            authorisationViewModel.authorised = sharedPreferences.getBoolean("authorised", false)

            if (authorisationViewModel.authorised) {
                if (authorisationViewModel.token != null) {
                    editor.apply {
                        putString("token", authorisationViewModel.token)
                        apply()
                    }
                }
                else {
                    authorisationViewModel.token = sharedPreferences.getString("token", null)
                }
            }

            NavHost(
                navController = navController,
                startDestination = BSUIRJournalScreen.Authorisation.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = BSUIRJournalScreen.Authorisation.name) {

                    Log.d("MyUi", "Authorised: ${authorisationViewModel.authorised}")
                    Log.d("MyUi", "StateSize: ${state.subjectsStates.size}")

                    if (!authorisationViewModel.authorised) {

                        Log.d("MyUi", "The problem1")
                        editor.apply {
                            putString("authorised", null)
                            putString("username", null)
                            putString("token", null)
                            putString("currentGroup", null)
                            putInt("subgroup", 0)
                            apply()
                        }
                        onEvent(SubjectStateEvent.DeleteAllSubjectsStates)

                        AuthorisationScreen(
                            authorisationViewModel = authorisationViewModel,
                            navController = navController,
                            editor = editor,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else {
                        Log.d("MyUi", "The problem2")
                        authorisationViewModel.username = sharedPreferences.getString("username", null)
                        authorisationViewModel.token = sharedPreferences.getString("token", null)
                        navController.navigate(BSUIRJournalScreen.GroupList.name)
                    }
                }
                composable(route = BSUIRJournalScreen.GroupList.name) {

                    Log.d("MyUi", "The problem3")
                    HomeScreen(
                        navController = navController,
                        groupsUiState = groupsViewModel.groupsUiState,
                        authorisationViewModel = authorisationViewModel,
                        onEvent = onEvent,
                        sharedPreferences = sharedPreferences,
                        editor = editor,
                    )
                }
                composable(route = BSUIRJournalScreen.Schedule.name) {
                    val scheduleViewModel: ScheduleViewModel =
                        viewModel(factory = ScheduleViewModel.Factory)


                    if (sharedPreferences.getBoolean("authorised", false)) {
                        Log.d("MyUi", "The problem4")
                        MainScreen(
                            navController = navController,
                            scheduleUiState = scheduleViewModel.scheduleUiState,
                            authorisationViewModel = authorisationViewModel,
                            state = state,
                            onEvent = onEvent ,
                            sharedPreferences = sharedPreferences,
                            modifier = Modifier
                        )
                    }
                    else {
                        Log.d("MyUi", "The problem5")
                        ErrorScreen(modifier = Modifier.fillMaxSize())
                    }
                }
                composable(route = BSUIRJournalScreen.Notes.name) {
                    Log.d("MyUi", "The problem6")
                    if (sharedPreferences.getBoolean("authorised", false)) {
                        NoteScreen(
                            notesViewModel = notesViewModel,
                            authorisationViewModel = authorisationViewModel
                        )
                    }
                    else {
                        ErrorScreen(modifier = Modifier.fillMaxSize())
                    }
                }
                composable(route = BSUIRJournalScreen.Account.name) {
                    Log.d("MyUi", "The problem7")
                    AccountScreen(
                        authorisationViewModel = authorisationViewModel,
                        navController = navController,
                        state = state,
                        onEvent = onEvent,
                        editor = editor,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    )
}

