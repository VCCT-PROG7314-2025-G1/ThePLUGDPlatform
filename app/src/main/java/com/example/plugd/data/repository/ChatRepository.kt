package com.example.plugd.data.repository

import com.example.plugd.model.Channel
import com.example.plugd.model.Message
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepository(
    private val firebaseDb: FirebaseDatabase,
    private val userId: String // current user
) {

    private val channelsRef = firebaseDb.getReference("channels")
    private val messagesRef = firebaseDb.getReference("messages")
    private val userChannelsRef = firebaseDb.getReference("userChannels") // stores channel IDs joined by user

    // All channels
    fun getChannels(): Flow<List<Channel>> = flow {
        channelsRef.get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull { it.getValue(Channel::class.java) }
            emit(list)
        }.addOnFailureListener { emit(emptyList()) }
    }

    // Channels the user has joined
    fun getUserJoinedChannels(): Flow<List<Channel>> = flow {
        userChannelsRef.child(userId).get().addOnSuccessListener { snapshot ->
            val channelIds = snapshot.children.mapNotNull { it.getValue(String::class.java) }
            // fetch Channel objects from all channels
            channelsRef.get().addOnSuccessListener { chSnap ->
                val allChannels = chSnap.children.mapNotNull { it.getValue(Channel::class.java) }
                val joinedChannels = allChannels.filter { channelIds.contains(it.id) }
                emit(joinedChannels)
            }
        }.addOnFailureListener { emit(emptyList()) }
    }

    // Posts / feed for channels user joined
    fun getUserFeed(): Flow<List<Message>> = flow {
        getUserJoinedChannels().collect { joinedChannels ->
            val feedMessages = mutableListOf<Message>()
            joinedChannels.forEach { channel ->
                messagesRef.child(channel.id).get().addOnSuccessListener { msgSnap ->
                    val messages = msgSnap.children.mapNotNull { it.getValue(Message::class.java) }
                    feedMessages.addAll(messages)
                    emit(feedMessages.sortedByDescending { it.timestamp })
                }
            }
        }
    }

    // Send a message
    fun sendMessage(channelId: String, message: Message) {
        messagesRef.child(channelId).push().setValue(message)
    }
}