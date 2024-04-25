package com.example.bsuirjournal2.ui.screens

import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bsuirjournal2.ui.JournalViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import com.example.bsuirjournal2.R
import com.example.bsuirjournal2.data.DataSource

enum class BSUIRJournalScreen() {
    Start,
    Main,
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
    viewModel: JournalViewModel = viewModel(),
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
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = BSUIRJournalScreen.Start.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = BSUIRJournalScreen.Start.name) {
                SelectGroupScreen(
                    groupNumberOptions = remember {
                        mutableStateOf(DataSource.groupNumberOptions)
                    },
                    onGroupCardClicked = {
                        viewModel.setGroup(it)
                        navController.navigate(BSUIRJournalScreen.Main.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = BSUIRJournalScreen.Main.name) {
                val context = LocalContext.current
                val groupsViewModel: GroupsViewModel =
                    viewModel(factory = GroupsViewModel.Factory)
                HomeScreen(groupsUiState = groupsViewModel.groupsUiState, retryAction = groupsViewModel::getGroupNumbers)
            }
        }
    }
}
