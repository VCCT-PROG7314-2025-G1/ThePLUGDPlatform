package com.example.plugd.model

import java.time.LocalDate
enum class AccountType { ARTIST, FAN, ORGANIZER }

data class User(
    val userId: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val dateOfBirth: String = "",
    val role: String? = "User",
    val profilePictureUrl: String? = null,
    val bio: String? = null,
    val interests: List<String> = emptyList()
)