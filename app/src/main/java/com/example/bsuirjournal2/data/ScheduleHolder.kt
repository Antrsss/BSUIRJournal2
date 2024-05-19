package com.example.bsuirjournal2.data

import com.example.bsuirjournal2.model.Schedule

object ScheduleHolder {
    var currentWeekSchedule: Schedule.Schedules? = null
    var currentSubgroup: Int? = 0
    var listOfSubjects: MutableList<String?> = mutableListOf()
    lateinit var uniqueSubjects: List<String?>
    fun createListOfSubjects() {
        var isSubjectInThisWeek: Boolean
        for (schedulesItem in currentWeekSchedule!!.monday!!) {
            isSubjectInThisWeek = false
            for (weekNumber in schedulesItem!!.weekNumber!!) {
                if (DataSource.currentWeek == weekNumber) {
                    isSubjectInThisWeek = true
                    break
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.tuesday!!) {
            isSubjectInThisWeek = false
            for (weekNumber in schedulesItem!!.weekNumber!!) {
                if (DataSource.currentWeek == weekNumber) {
                    isSubjectInThisWeek = true
                    break
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.wednesday!!) {
            isSubjectInThisWeek = false
            for (weekNumber in schedulesItem!!.weekNumber!!) {
                if (DataSource.currentWeek == weekNumber) {
                    isSubjectInThisWeek = true
                    break
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.thursday!!) {
            isSubjectInThisWeek = false
            for (weekNumber in schedulesItem!!.weekNumber!!) {
                if (DataSource.currentWeek == weekNumber) {
                    isSubjectInThisWeek = true
                    break
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.friday!!) {
            isSubjectInThisWeek = false
            for (weekNumber in schedulesItem!!.weekNumber!!) {
                if (DataSource.currentWeek == weekNumber) {
                    isSubjectInThisWeek = true
                    break
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.saturday!!) {
            isSubjectInThisWeek = false
            for (weekNumber in schedulesItem!!.weekNumber!!) {
                if (DataSource.currentWeek == weekNumber) {
                    isSubjectInThisWeek = true
                    break
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        uniqueSubjects = listOfSubjects.toList()
        uniqueSubjects = ScheduleHolder.listOfSubjects.distinct()
    }
}