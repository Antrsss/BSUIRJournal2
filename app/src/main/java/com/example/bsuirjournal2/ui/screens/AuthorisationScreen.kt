package com.example.bsuirjournal2.ui.screens

import android.content.SharedPreferences
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.bsuirjournal2.model.User
import com.example.bsuirjournal2.viewmodels.AuthorisationUiState
import com.example.bsuirjournal2.viewmodels.AuthorisationViewModel

@Composable
fun AuthorisationScreen(
    authorisationViewModel: AuthorisationViewModel,
    showFailRegDialog: Boolean,
    showFailAuthDialog: Boolean,
    showSuccessDialog: Boolean,
    isExitVisible: Boolean,
    navController: NavController,
    editor: SharedPreferences.Editor,
    modifier: Modifier
) {

    val username = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }

    val showFailToRegisterDialog = remember {
        mutableStateOf(showFailRegDialog)
    }

    val showSuccessToRegisterDialog = remember {
        mutableStateOf(showSuccessDialog)
    }

    val showFailToAuthoriseDialog = remember {
        mutableStateOf(showFailAuthDialog)
    }

    val isExitButtonVisible = remember {
        mutableStateOf(isExitVisible)
    }

    if (showFailToRegisterDialog.value) {
        FailToRegisterDialog(onDismissRequest = showFailToRegisterDialog)
    }

    if (showSuccessToRegisterDialog.value) {
        SuccessToRegisterDialog(
            onDismissRequest = showSuccessToRegisterDialog,
            authorisationViewModel = authorisationViewModel,
            username = username,
            password = password
        )
    }
    
    if (showFailToAuthoriseDialog.value) {
        FailToAuthorise(onDismissRequest = showFailToAuthoriseDialog)
    }

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
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "Пароль")}
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Button(
                onClick = {
                    authorisationViewModel.username = username.value
                    authorisationViewModel.password = password.value

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
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    authorisationViewModel.authoriseUser(
                        user = User(
                            id = 0,
                            username = username.value,
                            password = password.value
                        )
                    )
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Войти")
            }
        }
        if (isExitButtonVisible.value) {
            Button(
                onClick = {
                    authorisationViewModel.authorised = false
                    authorisationViewModel.authorisationUiState = AuthorisationUiState.Unauthorised
                    navController.navigate(BSUIRJournalScreen.Authorisation.name)
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Выйти")
            }
        }
    }
}

@Composable
fun FailToRegisterDialog(
    onDismissRequest: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = { },
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
        ) {
            Column (
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Ошибка регистрации!",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Данное имя пользователя уже используется или соединение с сервером прервано",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onDismissRequest.value = false },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(width = 80.dp, height = 60.dp)
                    ) {
                        Text(text = "Ок")
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessToRegisterDialog(
    onDismissRequest: MutableState<Boolean>,
    authorisationViewModel: AuthorisationViewModel,
    username: MutableState<String>,
    password: MutableState<String>
) {
    Dialog(
        onDismissRequest = { },
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
        ) {
            Column (
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Регистрация прошла успешно!",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Войдите в свой новый аккаунт",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            authorisationViewModel.authoriseUser(
                                user = User(
                                    id = 0,
                                    username = authorisationViewModel.username,
                                    password = authorisationViewModel.password
                                )
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(width = 100.dp, height = 60.dp)
                    ) {
                        Text(text = "Войти")
                    }
                }
            }
        }
    }
}

@Composable
fun FailToAuthorise(
    onDismissRequest: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = { },
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
        ) {
            Column (
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Ошибка авторизации!",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Неверный логин или пароль. Повторите попытку",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onDismissRequest.value = false },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(width = 80.dp, height = 60.dp)
                    ) {
                        Text(text = "Ок")
                    }
                }
            }
        }
    }
}