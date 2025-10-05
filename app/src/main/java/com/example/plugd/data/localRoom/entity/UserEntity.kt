package com.example.plugd.data.localRoom.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey var userId: String = "",  // Default values needed
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var password: String? = "" // stored locally for offline login
)