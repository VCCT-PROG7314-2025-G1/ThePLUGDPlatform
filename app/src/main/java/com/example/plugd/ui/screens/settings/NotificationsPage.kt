package com.example.plugd.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
/*import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.R
import com.example.plugd.model.NotificationType
import com.example.plugd.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsPage(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val user by viewModel.user.collectAsState()

    val pushEnabled = user?.pushEnabled ?: true
    val emailEnabled = user?.emailEnabled ?: false
    val smsEnabled = user?.smsEnabled ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.btn_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Manage how you receive notifications")
            Spacer(Modifier.height(16.dp))

            SwitchRow("Push Notifications", pushEnabled) {
                viewModel.updateNotificationType(NotificationType.EVENT_REMINDER, it)
            }

            SwitchRow("Email Notifications", emailEnabled) {
                viewModel.updateNotificationType(NotificationType.NEW_FOLLOWER, it)
            }

            SwitchRow("SMS Notifications", smsEnabled) {
                viewModel.updateNotificationType(NotificationType.GROUP_POST, it)
            }
        }
    }
}

@Composable
fun SwitchRow(x0: String, x1: Boolean, content: @Composable () -> updateNotificationType) {
    TODO("Not yet implemented")
}*/