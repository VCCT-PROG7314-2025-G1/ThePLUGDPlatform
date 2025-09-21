package com.example.plugd.remote.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class PlugdFirebaseService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        // send token to your backend
        Log.d("FCM", "New token: $token")
    }
    override fun onMessageReceived(message: RemoteMessage) {
        // show notification or handle data message
        Log.d("FCM", "Message: ${message.data}")
    }
}