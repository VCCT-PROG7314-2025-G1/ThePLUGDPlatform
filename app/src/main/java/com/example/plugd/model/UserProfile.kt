package com.example.plugd.model

data class UserProfile(
    val userId: String = "",
    val name: String? = "",
    val events: List<Event> = emptyList(),
    val username: String? = "",
    val email: String? = "",
    val phone: String? = null,
    val role: String? = "User",
    val bio: String? = null,
    val gender: String? = null,
    val location: String? = null,
    val followersCount: Int = 0,

    // Settings/preferences
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val biometricEnabled: Boolean = false,
    val pushEnabled: Boolean = true
)