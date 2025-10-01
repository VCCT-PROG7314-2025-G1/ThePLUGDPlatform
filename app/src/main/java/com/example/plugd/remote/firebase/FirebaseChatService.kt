package com.example.plugd.remote.firebase

import com.example.plugd.model.Message
import com.example.plugd.model.Channel
import com.google.firebase.database.*

class FirebaseChatService {
    private val db = FirebaseDatabase.getInstance("https://plugdapp-default-rtdb.firebaseio.com/").reference

    // Channels
    fun getChannels(onResult: (List<Channel>) -> Unit) {
        db.child("channels").get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull { it.getValue(Channel::class.java) }
            onResult(list)
        }.addOnFailureListener {
            onResult(emptyList())
        }
    }

    // Messages
    fun getMessages(channelId: String, onResult: (List<Message>) -> Unit) {
        db.child("messages").child(channelId).get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
            onResult(list)
        }.addOnFailureListener {
            onResult(emptyList())
        }
    }

    fun sendMessage(channelId: String, message: Message, onComplete: (() -> Unit)? = null) {
        db.child("messages").child(channelId).push().setValue(message)
            .addOnSuccessListener { onComplete?.invoke() }
            .addOnFailureListener { it.printStackTrace() }
    }

    // Listen for real-time updates
    fun observeMessages(channelId: String, onMessage: (Message) -> Unit) {
        db.child("messages").child(channelId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.getValue(Message::class.java)?.let { onMessage(it) }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}