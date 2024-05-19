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
package com.example.bsuirjournal2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bsuirjournal2.R
import com.example.bsuirjournal2.data.DataSource
import com.example.bsuirjournal2.ui.JournalViewModel
import java.io.File

@Composable
fun HomeScreen(
    viewModel: JournalViewModel,
    navController: NavHostController,
    groupsUiState: GroupsUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (groupsUiState) {
        is GroupsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is GroupsUiState.Success -> {
            DataSource.currentWeek = groupsUiState.currentWeek
            DataSource.groupNumberOptions.clear()
            DataSource.createGroupNumberOptions(groupsUiState.groups)

            val callChooseNumSubgroupDialog = remember { mutableStateOf(false) }
            GroupListScreen(
                groupNumberOptions = remember {
                    mutableStateOf(DataSource.groupNumberOptions)
                },
                onGroupCardClicked = {
                    viewModel.setGroup(it)
                    DataSource.currentGroup = it
                    callChooseNumSubgroupDialog.value = true
                },
                modifier = Modifier
                    .fillMaxSize()
            )
            if (callChooseNumSubgroupDialog.value == true) {
                ChooseNumSubgroupDialog(navController = navController, onDismissRequest = callChooseNumSubgroupDialog)
            }
        }
        is GroupsUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

