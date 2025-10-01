package com.example.plugd.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plugd.model.Channel
import com.example.plugd.model.Message
import com.example.plugd.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels: StateFlow<List<Channel>> get() = _channels

    private val _userJoinedChannels = MutableStateFlow<List<Channel>>(emptyList())
    val userJoinedChannels: StateFlow<List<Channel>> get() = _userJoinedChannels

    private val _userFeed = MutableStateFlow<List<Message>>(emptyList())
    val userFeed: StateFlow<List<Message>> get() = _userFeed

    init {
        loadChannels()
        loadUserJoinedChannels()
        loadUserFeed()
    }

    fun loadChannels() {
        viewModelScope.launch {
            repository.getChannels().collect { list ->
                _channels.value = list
            }
        }
    }

    fun loadUserJoinedChannels() {
        viewModelScope.launch {
            repository.getUserJoinedChannels().collect { list ->
                _userJoinedChannels.value = list
            }
        }
    }

    fun loadUserFeed() {
        viewModelScope.launch {
            repository.getUserFeed().collect { list ->
                _userFeed.value = list
            }
        }
    }

    fun sendMessage(channelId: String, message: Message) {
        repository.sendMessage(channelId, message)
    }
}