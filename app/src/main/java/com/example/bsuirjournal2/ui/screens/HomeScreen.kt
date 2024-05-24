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

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.bsuirjournal2.R
import com.example.bsuirjournal2.data.GroupApiHolder
import com.example.bsuirjournal2.roomdatabase.SubjectStateEvent
import com.example.bsuirjournal2.viewmodels.GroupsUiState

@Composable
fun HomeScreen(
    navController: NavHostController,
    groupsUiState: GroupsUiState,
    sharedPreferences: SharedPreferences,
    editor: SharedPreferences.Editor,
    onEvent: (SubjectStateEvent) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    when (groupsUiState) {
        is GroupsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is GroupsUiState.Success -> {

            var lastWeek = sharedPreferences.getInt("lastWeek", 0)

            if (groupsUiState.currentWeek != lastWeek) {
                editor.putInt("lastWeek", groupsUiState.currentWeek)
                onEvent(SubjectStateEvent.DeleteAllSubjectsStates)
            }

            GroupApiHolder.currentWeek = groupsUiState.currentWeek

            val currentGroup = sharedPreferences.getString("selectedGroup", null)
            GroupApiHolder.currentGroup = currentGroup

            val currentSubgroup = sharedPreferences.getInt("subgroup", 0)
            GroupApiHolder.currentSubgroup = currentSubgroup

            GroupApiHolder.groupNumberOptions.clear()
            GroupApiHolder.createGroupNumberOptions(groupsUiState.groups)

            if (currentGroup == null) {

                val callChooseNumSubgroupDialog = remember { mutableStateOf(false) }

                GroupListScreen(
                    groupNumberOptions = remember {
                        mutableStateOf(GroupApiHolder.groupNumberOptions)
                    },
                    onGroupCardClicked = {
                        editor.apply{
                            putString("selectedGroup", it)
                            apply()
                        }
                        GroupApiHolder.currentGroup = it
                        callChooseNumSubgroupDialog.value = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
                if (callChooseNumSubgroupDialog.value == true) {
                    ChooseNumSubgroupDialog(
                        navController = navController,
                        editor = editor,
                        onDismissRequest = callChooseNumSubgroupDialog
                    )
                }
            }
            else {
                navController.navigate(BSUIRJournalScreen.Schedule.name)
            }
        }
        is GroupsUiState.Error -> ErrorScreen(retryAction = retryAction, modifier = modifier.fillMaxSize())
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

@Composable
fun ChooseNumSubgroupDialog(
    navController: NavHostController,
    editor: SharedPreferences.Editor,
    onDismissRequest: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = { onDismissRequest.value = false }
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column (
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Выберите подгруппу:",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            GroupApiHolder.currentSubgroup = 1
                            editor.apply{
                                putInt("subgroup", 1)
                                apply()
                            }
                            navController.navigate(BSUIRJournalScreen.Schedule.name)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(width = 80.dp, height = 60.dp)
                    ) {
                        Text(text = "1")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedButton(
                        onClick = {
                            GroupApiHolder.currentSubgroup = 2
                            editor.apply{
                                putInt("subgroup", 2)
                                apply()
                            }
                            navController.navigate(BSUIRJournalScreen.Schedule.name)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(width = 80.dp, height = 60.dp)
                    ) {
                        Text(text = "2")
                    }
                }
            }
        }
    }
}

