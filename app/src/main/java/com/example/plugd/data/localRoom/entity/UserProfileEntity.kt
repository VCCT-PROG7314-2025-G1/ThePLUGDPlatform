package com.example.plugd.data.localRoom.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users_profile")
data class UserProfileEntity(
    @PrimaryKey val userId: String = UUID.randomUUID().toString(),
    val name: String,
    val username: String,
    val phone: String? = null,
    val bio: String? = null,
    val gender: String? = null,
    val location: String? = null,
    val followersCount: Int = 0,
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val biometricEnabled: Boolean = false
)