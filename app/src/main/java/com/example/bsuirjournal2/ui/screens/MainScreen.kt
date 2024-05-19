package com.example.bsuirjournal2.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bsuirjournal2.R
import com.example.bsuirjournal2.data.DataSource
import com.example.bsuirjournal2.data.ScheduleHolder
import com.example.bsuirjournal2.ui.JournalViewModel

@Composable
fun MainScreen(
    viewModel: JournalViewModel,
    scheduleUiState: ScheduleUiState,
    selectedGroup: String,
    retryAction: () -> Unit,
    modifier: Modifier
) {
    when (scheduleUiState) {
        is ScheduleUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is ScheduleUiState.Success -> {
            ScheduleHolder.currentWeekSchedule = scheduleUiState.schedule.schedules
            Log.d("ScheduleScreen", "Current week schedule: ${ScheduleHolder.currentWeekSchedule}")
            ScheduleHolder.createListOfSubjects()
            Log.d("ScheduleScreen", "${ScheduleHolder.listOfSubjects}")
            ScheduleScreen(
                listOfSubjects = ScheduleHolder.uniqueSubjects,
                currentWeek = DataSource.currentWeek,
                modifier = Modifier
            )
        }
        is ScheduleUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun ScheduleScreen(
    listOfSubjects: List<String?>,
    currentWeek: Int,
    modifier: Modifier
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Неделя $currentWeek",
            fontSize = 20.sp
            )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.width(280.dp)
        ) {
            Text(
                text = "Пн",
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Вт",
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Ср",
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Чт",
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Пт",
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Сб",
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Вс",
                textAlign = TextAlign.Center,
            )
        }
        LazyColumn (
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            items(listOfSubjects) { item ->
                item!!.let {
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var checkedNumber = remember { mutableStateOf(7) }
                        val paintersListState = remember {
                            mutableStateListOf(
                                "clearPainter",
                                "clearPainter",
                                "clearPainter",
                                "clearPainter",
                                "clearPainter",
                                "clearPainter",
                                "clearPainter",
                            )
                        }
                        val clearPainter = painterResource(id = R.drawable.task_free)
                        val donePainter = painterResource(id = R.drawable.task_done)
                        val arrowPainter = painterResource(id = R.drawable.task_arrow)
                        val cancelPainter = painterResource(id = R.drawable.task_cancel)

                        val paintersList: MutableList<Painter> = mutableListOf()

                        for (painterType in paintersListState) {
                            if (painterType == "donePainter") {
                                paintersList.add(donePainter)
                            }
                            else if (painterType == "arrowPainter") {
                                paintersList.add(arrowPainter)
                            }
                            else if (painterType == "cancelPainter") {
                                paintersList.add(cancelPainter)
                            }
                            else if (painterType == "clearPainter") {
                                paintersList.add(clearPainter)
                            }
                        }

                        CheckInRow (painters = paintersList, checkedNumber = checkedNumber)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item,
                            modifier = Modifier
                                .height(40.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                        if (checkedNumber.value != 7) {
                            ChooseCheckInActionDialog(
                                onDismissRequest = {checkedNumber.value = 7},
                                paintersState = paintersListState,
                                checkedNumber = checkedNumber
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckInRow(
    painters: MutableList<Painter>,
    checkedNumber: MutableState<Int>
) {
    val description = null
    Row() {
        Image(
            painter = painters[0],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedNumber.value = 0 }
        )
        Image(
            painter = painters[1],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedNumber.value = 1 }
        )
        Image(
            painter = painters[2],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedNumber.value = 2 }
        )
        Image(
            painter = painters[3],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedNumber.value = 3 }
        )
        Image(
            painter = painters[4],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedNumber.value = 4 }
        )
        Image(
            painter = painters[5],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedNumber.value = 5 }
        )
        Image(
            painter = painters[6],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedNumber.value = 6 }
        )
    }
}

@Composable
fun ChooseCheckInActionDialog(
    onDismissRequest: () -> Unit,
    paintersState: SnapshotStateList<String>,
    checkedNumber: MutableState<Int>
) {
    val clearPainter = painterResource(id = R.drawable.task_free)
    val donePainter = painterResource(id = R.drawable.task_done)
    val arrowPainter = painterResource(id = R.drawable.task_arrow)
    val cancelPainter = painterResource(id = R.drawable.task_cancel)
    val description = null
    Dialog(
        onDismissRequest = onDismissRequest
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
                    .padding(36.dp)
            ) {
                Text(
                    text = "Выберите:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            paintersState[checkedNumber.value] = "donePainter"
                            checkedNumber.value = 7
                        }
                    ){
                        Image(
                            painter = donePainter,
                            contentDescription = description,
                            modifier = Modifier
                                .size(40.dp, 40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Выполнить задачу",
                            modifier = Modifier
                                .height(40.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                    Row(
                        modifier = Modifier.clickable {
                            paintersState[checkedNumber.value] = "arrowPainter"
                            checkedNumber.value = 7
                        }
                    ){
                        Image(
                            painter = arrowPainter,
                            contentDescription = description,
                            modifier = Modifier
                                .size(40.dp, 40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Перенести задачу",
                            modifier = Modifier
                                .height(40.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                    Row(
                        modifier = Modifier.clickable {
                            paintersState[checkedNumber.value] = "cancelPainter"
                            checkedNumber.value = 7
                        }
                    ){
                        Image(
                            painter = cancelPainter,
                            contentDescription = description,
                            modifier = Modifier
                                .size(40.dp, 40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Отменить задачу",
                            modifier = Modifier
                                .height(40.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                    Row(
                        modifier = Modifier.clickable {
                            paintersState[checkedNumber.value] = "clearPainter"
                            checkedNumber.value = 7
                        }
                    ){
                        Image(
                            painter = clearPainter,
                            contentDescription = description,
                            modifier = Modifier
                                .size(40.dp, 40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Очистить",
                            modifier = Modifier
                                .height(40.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}


