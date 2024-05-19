package com.example.bsuirjournal2.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthorisationResponse(
    val message: String,
    val token: String
)
