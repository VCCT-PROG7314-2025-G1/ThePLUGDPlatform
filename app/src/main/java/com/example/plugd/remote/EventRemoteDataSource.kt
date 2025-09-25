package com.example.plugd.remote

import com.example.plugd.data.EventEntity

class EventRemoteDataSource(private val api: ApiService) {

    suspend fun getEvents(): List<EventEntity> {
        // Directly return list from API if it matches EventEntity
        return api.getEvents()
    }

    suspend fun addEvent(event: EventEntity) {
        api.addEvent(event)  // make sure API accepts same fields
    }
}