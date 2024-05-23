package com.example.bsuirjournal2.roomdatabase

sealed interface SubjectStateEvent {
    object SaveSubjectState: SubjectStateEvent
    /*data class SetMondayState(val monday: String, val subjectState: SubjectState): SubjectStateEvent
    data class SetTuesdayState(val tuesday: String): SubjectStateEvent
    data class SetWednesdayState(val wednesday: String): SubjectStateEvent
    data class SetThursdayState(val thursday: String): SubjectStateEvent
    data class SetFridayState(val friday: String): SubjectStateEvent
    data class SetSaturdayState(val saturday: String): SubjectStateEvent
    data class SetSundayState(val sunday: String): SubjectStateEvent*/
    object ShowDialog: SubjectStateEvent
    object HideDialog: SubjectStateEvent
    data class DeleteSubjectState(val subjectState: SubjectState): SubjectStateEvent
    data class DeleteAllSubjectsStates(val subjectState: SubjectState): SubjectStateEvent
    data class UpdateMonday(val monday: String, val roll: Int): SubjectStateEvent
    data class UpdateTuesday(val tuesday: String, val roll: Int): SubjectStateEvent
    data class UpdateWednesday(val wednesday: String, val roll: Int): SubjectStateEvent
    data class UpdateThursday(val thursday: String, val roll: Int): SubjectStateEvent
    data class UpdateFriday(val friday: String, val roll: Int): SubjectStateEvent
    data class UpdateSaturday(val saturday: String, val roll: Int): SubjectStateEvent
    data class UpdateSunday(val sunday: String, val roll: Int): SubjectStateEvent
}