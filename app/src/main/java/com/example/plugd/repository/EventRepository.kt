package com.example.plugd.repository

import com.example.plugd.data.EventEntity
import com.example.plugd.remote.EventRemoteDataSource

class EventRepository(private val remote: EventRemoteDataSource) {

    suspend fun getEvents(): List<EventEntity> {
        return remote.getEvents()  // already returns EventEntity
    }

    suspend fun addEvent(event: EventEntity) {
        remote.addEvent(event)
    }
}