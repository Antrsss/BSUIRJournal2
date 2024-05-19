package com.example.bsuirjournal2.data

import com.example.bsuirjournal2.model.Group
import com.example.bsuirjournal2.model.Schedule
import com.example.bsuirjournal2.network.GroupsApiService

/**
 * Repository that fetch mars photos list from marsApi.
 */
interface GroupNumbersRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getGroupNumbers(): List<Group>
    suspend fun getCurrentWeek(): Int
    suspend fun getGroupSchedule(groupNumber: String?): Schedule
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class NetworkGroupNumbersRepository(

    private val groupsApiService: GroupsApiService

) : GroupNumbersRepository {
    override suspend fun getGroupNumbers(): List<Group> = groupsApiService.getGroups()
    override suspend fun getCurrentWeek(): Int = groupsApiService.getCurrentWeek()
    override suspend fun getGroupSchedule(groupNumber: String?): Schedule = groupsApiService.getGroupSchedule(groupNumber)
}
