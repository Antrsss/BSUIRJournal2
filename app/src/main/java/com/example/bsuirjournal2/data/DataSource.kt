package com.example.bsuirjournal2.data

import com.example.bsuirjournal2.model.Group

object DataSource {
    val groupNumberOptions: MutableList<String> = mutableListOf()
    var currentGroup: String = ""
    var currentWeek: Int = 0
    /*fun getGroupSchedule(groupNumber: String) : Schedule {
        var i: Int = 0
        for (schedule in schedules) {
            i++
            if (schedule.id == currentGroup) {
                break
            }
        }
        return schedules[i]
    }
    fun groupSchedulesToDataSource() {
        for (schedule in schedules) {
            schedules.add(schedule)
        }
        schedules.toList()
    }*/
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
}