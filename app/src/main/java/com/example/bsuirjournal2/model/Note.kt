package com.example.bsuirjournal2.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Long,
    val author: String?,
    val content: String,
)