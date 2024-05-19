/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bsuirjournal2.network

import com.example.bsuirjournal2.model.Group
import com.example.bsuirjournal2.model.Schedule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A public interface that exposes the [getGroups] method
 */
interface GroupsApiService {
    /**
     * Returns a [List] of [Group] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "photos" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("student-groups")
    suspend fun getGroups(): List<Group>
    @GET("schedule/current-week")
    suspend fun getCurrentWeek(): Int
    @GET("schedule")
    /** Получение расписания группы **/
    suspend fun getGroupSchedule(@Query("studentGroup") groupNumber: String?): Schedule
}
