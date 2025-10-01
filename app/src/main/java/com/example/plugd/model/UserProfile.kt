package com.example.plugd.model

data class UserProfile(
    val userId: String,
    val name: String,
    val username: String,
    val email: String,
    val phone: String? = null,
    val role: String = "User",
    val bio: String? = null,
    val gender: String? = null,
    val location: String? = null,
    val followersCount: Int = 0
)