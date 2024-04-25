package com.example.bsuirjournal2.data

import com.example.bsuirjournal2.model.Group
import com.example.bsuirjournal2.network.GroupsApiService

/**
 * Repository that fetch mars photos list from marsApi.
 */
interface GroupNumbersRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getGroupNumbers(): List<Group>
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class NetworkGroupNumbersRepository(
    private val groupsApiService: GroupsApiService
) : GroupNumbersRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun getGroupNumbers(): List<Group> = groupsApiService.getGroups()
}
