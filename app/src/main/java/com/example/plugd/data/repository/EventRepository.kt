import com.example.plugd.data.localRoom.dao.EventDao
import com.example.plugd.data.localRoom.entity.EventEntity
import com.example.plugd.data.remoteFireStore.EventRemoteDataSource
import kotlinx.coroutines.flow.Flow

class EventRepository(
    private val eventDao: EventDao,
    private val eventRemote: EventRemoteDataSource
) {
    // Offline-first flow (UI reads from Room)
    val events: Flow<List<EventEntity>> = eventDao.getAllEvents()

    // Add new event -> Room + API
    suspend fun addEvent(event: EventEntity) {
        eventDao.insertEvent(event)      // save locally first
        try {
            eventRemote.addEvent(event)  // push to API
        } catch (e: Exception) {
            // TODO: Optionally mark event as "pending sync"
            e.printStackTrace()
        }
    }

    suspend fun loadEvents() {
        try {
            val remoteEvents = eventRemote.getEvents()
            eventDao.clearEvents()
            eventDao.insertAll(remoteEvents)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Refresh from API -> overwrite local Room
    suspend fun syncEvents() {
        try {
            val remoteEvents = eventRemote.getEvents()
            eventDao.clearEvents()
            eventDao.insertAll(remoteEvents)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}












/*   working   package com.example.plugd.data.repository

import com.example.plugd.data.localRoom.dao.EventDao
import com.example.plugd.data.localRoom.entity.EventEntity
import com.example.plugd.data.remoteFireStore.EventRemoteDataSource
import kotlinx.coroutines.flow.Flow

class EventRepository(
    private val eventDao: EventDao,
    private val eventRemote: EventRemoteDataSource
) {
    // Offline-first flow (UI reads from Room)
    val events: Flow<List<EventEntity>> = eventDao.getAllEvents()

    // Add new event -> Room + API
    suspend fun addEvent(event: EventEntity) {
        eventDao.insertEvent(event)      // save locally first
        try {
            eventRemote.addEvent(event)  // push to API
        } catch (e: Exception) {
            // TODO: Optionally mark event as "pending sync"
            e.printStackTrace()
        }
    }

    suspend fun loadEvents() {
        try {
            val remoteEvents = eventRemote.getEvents()
            eventDao.clearEvents()
            eventDao.insertAll(remoteEvents)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Refresh from API -> overwrite local Room
    suspend fun syncEvents() {
        try {
            val remoteEvents = eventRemote.getEvents()
            eventDao.clearEvents()
            eventDao.insertAll(remoteEvents)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}*/












/*package com.example.plugd.data.repository

import com.example.plugd.data.localRoom.dao.EventDao
import com.example.plugd.data.localRoom.entity.EventEntity
import com.example.plugd.data.remoteFireStore.EventRemoteDataSource
import kotlinx.coroutines.flow.Flow

class EventRepository(
    private val eventDao: EventDao,
    private val eventRemote: EventRemoteDataSource
) {
    val events: Flow<List<EventEntity>> = eventDao.getAllEvents()

    suspend fun addEvent(event: EventEntity) {
        eventDao.insertEvent(event)             // save locally
        eventRemote.addEvent(event)             // push to Firestore
    }

    suspend fun loadEvents() {
        val remoteEvents = eventRemote.getEvents()
        eventDao.insertAll(remoteEvents)        // sync Firestore down to Room
    }
}*/