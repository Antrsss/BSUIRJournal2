package com.example.bsuirjournal2.ui.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bsuirjournal2.data.GroupApiHolder
import com.example.bsuirjournal2.model.User
import com.example.bsuirjournal2.roomdatabase.State
import com.example.bsuirjournal2.roomdatabase.SubjectState
import com.example.bsuirjournal2.roomdatabase.SubjectStateEvent
import com.example.bsuirjournal2.viewmodels.AuthorisationViewModel

@Composable
fun AccountScreen(
    authorisationViewModel: AuthorisationViewModel,
    onEvent: (SubjectStateEvent) -> Unit,
    state: State,
    navController: NavController,
    editor: SharedPreferences.Editor,
    modifier: Modifier
) {

    val username = remember{mutableStateOf("")}
    val password = remember{mutableStateOf("")}

    var isRegistrationButtonVisible by remember { mutableStateOf(true) }

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
            onValueChange = { username.value = it },
            label = { Text(text = "Логин")}
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text = "Пароль")}
        )
        Spacer(modifier = Modifier.height(4.dp))
        Column {
            if (isRegistrationButtonVisible) {
                Button(
                    onClick = {
                        editor.apply {
                            editor.putBoolean("authorised", true)
                            editor.putString("username", username.value)
                            editor.putString("password", password.value)
                            apply()
                        }
                        isRegistrationButtonVisible = false
                        authorisationViewModel.registerUser(
                            user = User(
                                id = 0,
                                username = username.value,
                                password = password.value
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Зарегестрироваться")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    editor.apply {
                        editor.putBoolean("authorised", true)
                        apply()
                    }
                    authorisationViewModel.authoriseUser(
                        user = User(
                            id = 0,
                            username = username.value,
                            password = password.value
                        )
                    )
                    authorisationViewModel.authorised = true
                    navController.navigate(BSUIRJournalScreen.GroupList.name)
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Войти")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    editor.apply {
                        putString("authorised", null)
                        putString("username", null)
                        putString("token", null)
                        putString("currentGroup", null)
                        putInt("subgroup", 0)
                        apply()
                    }
                    onEvent(SubjectStateEvent.DeleteAllSubjectsStates)
                    state.subjectsStates = emptyList<SubjectState>()
                    GroupApiHolder.uniqueSubjects = emptyList()
                    authorisationViewModel.authorised = false
                    navController.navigate(BSUIRJournalScreen.Authorisation.name)
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Выйти")
            }
        }
    }
}