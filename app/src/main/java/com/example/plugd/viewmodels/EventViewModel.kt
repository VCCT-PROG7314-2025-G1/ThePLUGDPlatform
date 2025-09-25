package com.example.plugd.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plugd.data.EventEntity
import com.example.plugd.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    private val _relevantEvents = MutableStateFlow<List<EventEntity>>(emptyList())
    val relevantEvents: StateFlow<List<EventEntity>> = _relevantEvents

    init {
        viewModelScope.launch {
            try {
                _relevantEvents.value = repository.getEvents() // load initial events
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                repository.addEvent(event)
                _relevantEvents.value = repository.getEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun syncEventsToLocal() {
        viewModelScope.launch {
            try {
                _relevantEvents.value = repository.getEvents()
            } catch (e: Exception) {
                e.printStackTrace() // prevents app crash if server fails
            }
        }
    }
}