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

package com.example.bsuirjournal2.model

import kotlinx.serialization.Serializable

/**
 * This data class defines a Mars photo which includes an ID, and the image URL.
 */

/*val id: String,
@SerialName(value = "img_src")
val imgSrc: String*/
@Serializable
data class Group(
    val name: String,
    val facultyId: Int,
    val facultyAbbrev: String,
    val facultyName: String,
    val specialityDepartmentEducationFormId: Int,
    val specialityName: String,
    val specialityAbbrev: String,
    val course: Int,
    val id: Int,
    val calendarId: String,
)
