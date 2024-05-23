package com.example.bsuirjournal2.data

import android.util.Log
import com.example.bsuirjournal2.model.Group
import com.example.bsuirjournal2.model.Schedule

object GroupApiHolder {
    var currentWeekSchedule: Schedule.Schedules? = null
    var currentSubgroup: Int? = null
    var listOfSubjects: MutableList<String?> = mutableListOf()
    lateinit var uniqueSubjects: List<String?>

    val groupNumberOptions: MutableList<String> = mutableListOf()
    var currentGroup: String? = null
    var currentWeek: Int? = null

    fun createGroupNumberOptions(groups: List<Group>) {
        for (group in groups) {
            groupNumberOptions.add(group.name)
        }
        groupNumberOptions.toList()
    }
    fun search(text: String): List<String> {
        return groupNumberOptions.filter {
            it.startsWith(text)
        }
    }
    fun createListOfSubjects() {
        var isSubjectInThisWeek: Boolean
        for (schedulesItem in currentWeekSchedule!!.monday!!) {
            isSubjectInThisWeek = false
            schedulesItem?.weekNumber?.let {
                for (weekNumber in it) {
                    if (currentWeek == weekNumber) {
                        isSubjectInThisWeek = true
                        break
                    }
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.tuesday!!) {
            isSubjectInThisWeek = false
            schedulesItem?.weekNumber?.let {
                for (weekNumber in it) {
                    if (currentWeek == weekNumber) {
                        isSubjectInThisWeek = true
                        break
                    }
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.wednesday!!) {
            isSubjectInThisWeek = false
            schedulesItem?.weekNumber?.let {
                for (weekNumber in it) {
                    if (currentWeek == weekNumber) {
                        isSubjectInThisWeek = true
                        break
                    }
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.thursday!!) {
            isSubjectInThisWeek = false
            schedulesItem?.weekNumber?.let {
                for (weekNumber in it) {
                    if (currentWeek == weekNumber) {
                        isSubjectInThisWeek = true
                        break
                    }
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.friday!!) {
            isSubjectInThisWeek = false
            schedulesItem?.weekNumber?.let {
                for (weekNumber in it) {
                    if (currentWeek == weekNumber) {
                        isSubjectInThisWeek = true
                        break
                    }
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        for (schedulesItem in currentWeekSchedule!!.saturday!!) {
            isSubjectInThisWeek = false
            schedulesItem?.weekNumber?.let {
                for (weekNumber in it) {
                    if (currentWeek == weekNumber) {
                        isSubjectInThisWeek = true
                        break
                    }
                }
            }
            if (isSubjectInThisWeek == true && (schedulesItem.numSubgroup == currentSubgroup || schedulesItem.numSubgroup == 0)) {
                listOfSubjects.add(schedulesItem.subject)
            }
        }
        uniqueSubjects = listOfSubjects.toList()
        uniqueSubjects = listOfSubjects.distinct()
    }
}