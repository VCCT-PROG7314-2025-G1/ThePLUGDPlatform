package com.example.plugd.data.localRoom.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String = UUID.randomUUID().toString(),
    val name: String,
    val username: String,
    val email: String,
    val password: String?,  // email/password login
    val phone: String? = null,
    val role: String = "User",
    val bio: String? = null,
    val gender: String? = null,
    val location: String? = null,
    val followersCount: Int = 0
)