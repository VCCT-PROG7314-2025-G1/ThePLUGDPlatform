package com.example.plugd.model

import java.time.Instant

data class Event(
    val eventId: String,
    val eventName: String,
    val description: String,
    val category: String,
    val startDateTime: Instant,
    val endDateTime: Instant,
    val location: String,
    val organizerId: String
)