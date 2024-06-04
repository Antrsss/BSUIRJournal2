package com.example.bsuirjournal2.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bsuirjournal2.roomdatabase.State
import com.example.bsuirjournal2.roomdatabase.SubjectSateDao
import com.example.bsuirjournal2.roomdatabase.SubjectState
import com.example.bsuirjournal2.roomdatabase.SubjectStateEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubjectStateViewModel(
    private val dao: SubjectSateDao
): ViewModel() {

    private val _subjectsStates = dao.getSubjectsStates()
    private val _state = MutableStateFlow(State())
    val state = combine(_state, _subjectsStates) {state, subjectsStates ->
        state.copy(
            subjectsStates = subjectsStates
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), State())

    fun onEvent(event: SubjectStateEvent) {
        when(event) {
            is SubjectStateEvent.DeleteSubjectState -> {
                viewModelScope.launch {
                    dao.deleteSubjectState(event.subjectState)
                }
            }
            SubjectStateEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingSubjectState = false
                ) }
            }
            SubjectStateEvent.SaveSubjectState -> {
                val monday = state.value.monday
                val tuesday = state.value.tuesday
                val wednesday = state.value.wednesday
                val thursday = state.value.thursday
                val friday = state.value.friday
                val saturday = state.value.saturday
                val sunday = state.value.sunday
                val rollNo = state.value.rollNo

                if (monday.isBlank() || tuesday.isBlank() ||
                    wednesday.isBlank() || thursday.isBlank() ||
                    friday.isBlank() || saturday.isBlank() ||
                    sunday.isBlank()) {
                    return
                }

                val subjectState = SubjectState(
                    monday = monday,
                    tuesday = tuesday,
                    wednesday = wednesday,
                    thursday = thursday,
                    friday = friday,
                    saturday = saturday,
                    sunday = sunday,
                    rollNo = rollNo
                )
                viewModelScope.launch {
                    dao.upsertSubjectState(subjectState)
                }
                Log.d("database", "upserted")
                _state.update { it.copy(
                    isAddingSubjectState = false,
                    monday = "clearPainter",
                    tuesday = "clearPainter",
                    wednesday = "clearPainter",
                    thursday = "clearPainter",
                    friday = "clearPainter",
                    saturday = "clearPainter",
                    sunday = "clearPainter",
                    rollNo = rollNo + 1
                ) }
            }
            is SubjectStateEvent.UpdateMonday -> {
                viewModelScope.launch {
                    dao.updateMonday(event.monday, event.roll)
                }
            }
            is SubjectStateEvent.UpdateTuesday -> {
                viewModelScope.launch {
                    dao.updateTuesday(event.tuesday, event.roll)
                }
            }
            is SubjectStateEvent.UpdateWednesday -> {
                viewModelScope.launch {
                    dao.updateWednesday(event.wednesday, event.roll)
                }
            }
            is SubjectStateEvent.UpdateThursday -> {
                viewModelScope.launch {
                    dao.updateThursday(event.thursday, event.roll)
                }
            }
            is SubjectStateEvent.UpdateFriday -> {
                viewModelScope.launch {
                    dao.updateFriday(event.friday, event.roll)
                }
            }
            is SubjectStateEvent.UpdateSaturday -> {
                viewModelScope.launch {
                    dao.updateSaturday(event.saturday, event.roll)
                }
            }
            is SubjectStateEvent.UpdateSunday -> {
                viewModelScope.launch {
                    dao.updateSunday(event.sunday, event.roll)
                }
            }
            is SubjectStateEvent.DeleteAllSubjectsStates -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        isAddingSubjectState = false,
                        monday = "clearPainter",
                        tuesday = "clearPainter",
                        wednesday = "clearPainter",
                        thursday = "clearPainter",
                        friday = "clearPainter",
                        saturday = "clearPainter",
                        sunday = "clearPainter",
                        rollNo = 0
                    ) }
                    dao.deleteAllSubjectsStates()
                }
            }
            SubjectStateEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingSubjectState = true
                ) }
            }
        }
    }
}