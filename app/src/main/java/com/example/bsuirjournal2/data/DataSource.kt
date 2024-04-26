package com.example.bsuirjournal2.data

import com.example.bsuirjournal2.model.Group

object DataSource {
    val groupNumberOptions: MutableList<String> = mutableListOf()

    var currentGroup: String = ""
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