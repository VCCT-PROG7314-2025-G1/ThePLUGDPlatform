package com.example.plugd.model

import java.time.Instant

data class Follow(val followerId: String, val followeeId: String)

data class Like(val userId: String, val eventId: String)

data class Comment(
    val commentId: String,
    val userId: String,
    val eventId: String,
    val text: String,
    val dateTime: Instant
)

data class Share(
    val userId: String,
    val eventId: String,
    val platform: String, // e.g. "whatsapp", "instagram"
    val dateTime: Instant
)