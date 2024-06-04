package com.example.bsuirjournal2.roomdatabase

data class State(
    var subjectsStates: List<SubjectState> = emptyList(),
    val monday: String = "clearPainter",
    val tuesday: String = "clearPainter",
    val wednesday: String = "clearPainter",
    val thursday: String = "clearPainter",
    val friday: String = "clearPainter",
    val saturday: String = "clearPainter",
    val sunday: String = "clearPainter",
    val rollNo: Int = 0,
    val isAddingSubjectState: Boolean = false,
)
