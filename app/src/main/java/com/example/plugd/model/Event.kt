package com.example.plugd.model

import java.time.Instant

data class Event(
    val eventId: String,
    val eventName: String,
    val description: String,
    val category: String,               // e.g. Music, Sports, Food
    val startDateTime: Instant,
    val endDateTime: Instant,
    val location: String,               // you can add lat/lng fields if needed
    val ticketPrice: Double,            // 0.0 == free
    val capacity: Int,
    val organizerId: String
)