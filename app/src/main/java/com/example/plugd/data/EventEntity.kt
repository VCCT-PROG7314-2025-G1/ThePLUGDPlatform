package com.example.plugd.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    val id: Long,
    val name: String,
    val category: String?,   // nullable if API doesn’t provide
    val date: Long?,         // timestamp in millis
    val location: String?,
    val description: String,
    val createdBy: String
)