package com.example.bsuirjournal2.ui.screens

import android.content.SharedPreferences
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bsuirjournal2.R
import com.example.bsuirjournal2.data.GroupApiHolder
import com.example.bsuirjournal2.roomdatabase.State
import com.example.bsuirjournal2.roomdatabase.SubjectStateEvent
import com.example.bsuirjournal2.viewmodels.ScheduleUiState

@Composable
fun MainScreen(
    state: State,
    onEvent: (SubjectStateEvent) -> Unit,
    sharedPreferences: SharedPreferences,
    scheduleUiState: ScheduleUiState,
    retryAction: () -> Unit,
    modifier: Modifier
) {
    Log.d("MyUi", "MainScreen!")
    when (scheduleUiState) {
        is ScheduleUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is ScheduleUiState.Success -> {

            GroupApiHolder.currentWeekSchedule = scheduleUiState.schedule.schedules
            GroupApiHolder.createListOfSubjects()

            Log.d("MyUi", "Schedule screen achieved")
            Log.d("database", "${state.subjectsStates.size} < ${GroupApiHolder.uniqueSubjects.size}")

            if (state.subjectsStates.size < GroupApiHolder.uniqueSubjects.size) {
                onEvent(SubjectStateEvent.SaveSubjectState)
            }

            Log.d("database", "New ${state.subjectsStates.size} < ${GroupApiHolder.uniqueSubjects.size}")

            ScheduleScreen(
                state = state,
                onEvent = onEvent,
                listOfSubjects = GroupApiHolder.uniqueSubjects,
                currentWeek = GroupApiHolder.currentWeek,
            )
        }
        is ScheduleUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}


@Composable
fun ScheduleScreen(
    state: State,
    onEvent: (SubjectStateEvent) -> Unit,
    listOfSubjects: List<String?>,
    currentWeek: Int?,
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
            items(state.subjectsStates) { item ->
                item!!.let {
                    Log.d("MyUi", "Item Success!")
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val clearPainter = painterResource(id = R.drawable.task_free)
                        val donePainter = painterResource(id = R.drawable.task_done)
                        val arrowPainter = painterResource(id = R.drawable.task_arrow)
                        val cancelPainter = painterResource(id = R.drawable.task_cancel)

                        val rollNo = state.subjectsStates.indexOf(it)
                        val currentSubject = listOfSubjects[rollNo]
                        Log.d("MyUi", "CurrentSubject ${state.subjectsStates.indexOf(it)}")
                        val subjectState = item

                        val subjectPaintersState = mutableListOf(
                            subjectState.monday,
                            subjectState.tuesday,
                            subjectState.wednesday,
                            subjectState.thursday,
                            subjectState.friday,
                            subjectState.saturday,
                            subjectState.sunday
                        )

                        Log.d("MyUi", "States Success")

                        var checkedDay = remember {
                            mutableStateOf(Pair(rollNo, 7))
                        }

                        val subjectPaintersList: MutableList<Painter> = mutableListOf()

                        for (painterState in subjectPaintersState) {
                            if (painterState == "donePainter") {
                                subjectPaintersList.add(donePainter)
                            }
                            else if (painterState == "arrowPainter") {
                                subjectPaintersList.add(arrowPainter)
                            }
                            else if (painterState == "cancelPainter") {
                                subjectPaintersList.add(cancelPainter)
                            }
                            else if (painterState == "clearPainter") {
                                subjectPaintersList.add(clearPainter)
                            }
                        }

                        SquaresRow (painters = subjectPaintersList, checkedDay = checkedDay)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentSubject.toString(),
                            modifier = Modifier
                                .height(40.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                        if (checkedDay.value.second != 7) {
                            ChooseCheckSquareDialog(
                                onDismissRequest = { checkedDay.value = Pair(checkedDay.value.first, 7) },
                                onEvent = onEvent,
                                checkedDay = checkedDay
                            )
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun SquaresRow(
    painters: MutableList<Painter>,
    checkedDay: MutableState<Pair<Int, Int>>,
) {
    val description = null
    Row() {
        Image(
            painter = painters[0],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable {
                    checkedDay.value = Pair(checkedDay.value.first, 0)
                }
        )
        Image(
            painter = painters[1],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedDay.value = Pair(checkedDay.value.first, 1) }
        )
        Image(
            painter = painters[2],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedDay.value = Pair(checkedDay.value.first, 2) }
        )
        Image(
            painter = painters[3],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedDay.value = Pair(checkedDay.value.first, 3) }
        )
        Image(
            painter = painters[4],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedDay.value = Pair(checkedDay.value.first, 4) }
        )
        Image(
            painter = painters[5],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedDay.value = Pair(checkedDay.value.first, 5) }
        )
        Image(
            painter = painters[6],
            contentDescription = description,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .clickable { checkedDay.value = Pair(checkedDay.value.first, 6) }
        )
    }
}

@Composable
fun ChooseCheckSquareDialog(
    onDismissRequest: () -> Unit,
    onEvent: (SubjectStateEvent) -> Unit,
    checkedDay: MutableState<Pair<Int, Int>>
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
                            updateDayState(checkedDay, "donePainter", onEvent)
                            checkedDay.value = Pair(checkedDay.value.first, 7)
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
                            updateDayState(checkedDay, "arrowPainter", onEvent)
                            checkedDay.value = Pair(checkedDay.value.first, 7)
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
                            updateDayState(checkedDay, "cancelPainter", onEvent)
                            checkedDay.value = Pair(checkedDay.value.first, 7)
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
                            updateDayState(checkedDay, "clearPainter", onEvent)
                            checkedDay.value = Pair(checkedDay.value.first, 7)
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

fun updateDayState(checkedDay: MutableState<Pair<Int, Int>>, painterState: String, onEvent: (SubjectStateEvent) -> Unit) {
    if (checkedDay.value.second == 0) {
        onEvent(SubjectStateEvent.UpdateMonday(painterState, checkedDay.value.first))
    }
    else if (checkedDay.value.second == 1) {
        onEvent(SubjectStateEvent.UpdateTuesday(painterState, checkedDay.value.first))
    }
    else if (checkedDay.value.second == 2) {
        onEvent(SubjectStateEvent.UpdateWednesday(painterState, checkedDay.value.first))
    }
    else if (checkedDay.value.second == 3) {
        onEvent(SubjectStateEvent.UpdateThursday(painterState, checkedDay.value.first))
    }
    else if (checkedDay.value.second == 4) {
        onEvent(SubjectStateEvent.UpdateFriday(painterState, checkedDay.value.first))
    }
    else if (checkedDay.value.second == 5) {
        onEvent(SubjectStateEvent.UpdateSaturday(painterState, checkedDay.value.first))
    }
    else if (checkedDay.value.second == 6) {
        onEvent(SubjectStateEvent.UpdateSunday(painterState, checkedDay.value.first))
    }
}

