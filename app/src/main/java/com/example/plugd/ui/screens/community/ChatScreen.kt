package com.example.plugd.ui.screens.community

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.plugd.R
import com.example.plugd.model.Message
import com.example.plugd.viewmodels.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    channelId: String,
    channelName: String,
    viewModel: ChatViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var messageText by remember { mutableStateOf("") }

    val messages by viewModel.messages.collectAsState()

    // Load messages from repository
    LaunchedEffect(channelId) {
        viewModel.loadMessages(channelId)
    }

    // Function to show local notification
    fun showNotification(message: Message) {
        val channelIdNotif = "plugd_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelIdNotif,
                "PLUGD Chat",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelIdNotif)
            .setSmallIcon(R.drawable.plugd_icon)
            .setContentTitle("New message from ${message.senderName}")
            .setContentText(message.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = channelName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(messages) { msg ->
                Text(
                    text = "${msg.senderName}: ${msg.content}",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {

            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                if (messageText.isNotBlank()) {
                    val message = Message(
                        id = System.currentTimeMillis().toString(),
                        channelId = channelId,
                        senderId = "CURRENT_USER_ID", // Replace with actual user ID
                        senderName = "You", // Replace with actual user name
                        content = messageText,
                        timestamp = System.currentTimeMillis()
                    )

                    // Send message via ViewModel
                    viewModel.sendMessage(channelId, message)

                    // Clear input
                    messageText = ""
                }
            }) {
                Text("Send")
            }
        }
    }

    // Listen for new messages and show notifications
    LaunchedEffect(channelId) {
        viewModel.repository.observeRealtimeMessages(channelId) { newMessage ->
            if (newMessage.senderId != "CURRENT_USER_ID") { // Only notify for others
                showNotification(newMessage)
            }
        }
    }
}