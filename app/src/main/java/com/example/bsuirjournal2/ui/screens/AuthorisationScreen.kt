package com.example.bsuirjournal2.ui.screens

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bsuirjournal2.model.User
import com.example.bsuirjournal2.viewmodels.GroupsViewModel

@Composable
fun AuthorisationScreen(
    groupsViewModel: GroupsViewModel,
    navController: NavHostController,
    sharedPreferences: SharedPreferences,
    editor: SharedPreferences.Editor,
    modifier: Modifier
) {
    Log.d("MyUi", "AutScreen")
    if (sharedPreferences.getBoolean("authorised", false) == false) {
        val username = remember{mutableStateOf("")}
        val password = remember{mutableStateOf("")}
        Column (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 150.dp)
        ) {
            Text(
                text = "Регистрация",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Button(
                    onClick = {
                        groupsViewModel.registerUser(
                            user = User(
                                id = 0,
                                username = username.value,
                                password = password.value
                            )
                        )
                        navController.navigate(BSUIRJournalScreen.GroupList.name)
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Зарегестрироваться")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        groupsViewModel.authoriseUser(
                            user = User(
                                id = 0,
                                username = username.value,
                                password = password.value
                            )
                        )
                        editor.apply {
                            putBoolean("authorised", true)
                            apply()
                        }
                        navController.navigate(BSUIRJournalScreen.GroupList.name)
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Войти")
                }
            }
        }
    }
    else {
        navController.navigate(BSUIRJournalScreen.GroupList.name)
    }
}