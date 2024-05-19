package com.example.bsuirjournal2.model

import com.google.gson.annotations.SerializedName

data class Schedule(
    val currentPeriod: String?,
    val currentTerm: String?,
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val studentGroup: StudentGroup?,
    val schedules: Schedules?,
    val previousSchedules: Schedules?,
    val exams: List<Schedules.SchedulesItem>?
) {
    data class StudentGroup(
        val name: String,
        val facultyId: Int,
        val facultyAbbrev: String,
        val specialityDepartmentEducationFormId: Int,
        val specialityName: String,
        val specialityAbbrev: String,
        val course: Int,
        val id: Int,
        val calendarId: String?,
        val educationDegree: Int,
    )

    data class Schedules(
        @SerializedName("Понедельник")
        val monday: List<SchedulesItem>?,
        @SerializedName("Вторник")
        val tuesday: List<SchedulesItem>?,
        @SerializedName("Среда")
        val wednesday: List<SchedulesItem>?,
        @SerializedName("Четверг")
        val thursday: List<SchedulesItem>?,
        @SerializedName("Пятница")
        val friday: List<SchedulesItem>?,
        @SerializedName("Суббота")
        val saturday: List<SchedulesItem>?,
    ) {
        data class SchedulesItem(
            val lessonTypeAbbrev: String?,
            val numSubgroup: Int,
            val subject: String?,
            val weekNumber: List<Int>?,
            val dateLesson: String?,
            val startLessonDate: String?,
            val endLessonDate: String?,
        )
    }
}