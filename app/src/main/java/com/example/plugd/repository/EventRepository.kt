package com.example.plugd.repository

import com.example.plugd.remote.ApiService

class EventRepository(private val api: ApiService) {
    suspend fun getEvents() = api.getEvents()
}