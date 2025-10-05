/*package com.example.plugd.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.plugd.ui.auth.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun ChangePasswordPage(viewModel: AuthViewModel) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val uiState = viewModel.uiState
    var message by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState) {
        message = when (uiState) {
            is AuthUiState.Success -> "Password updated successfully."
            is AuthUiState.Error -> uiState.message
            else -> null
        }
        if (uiState is AuthUiState.Success || uiState is AuthUiState.Error) {
            delay(3000)
            viewModel.resetUiState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Current Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm New Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (newPassword == confirmPassword) {
                    viewModel.changePassword(currentPassword, newPassword)
                } else {
                    message = "New passwords do not match"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Password")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = if (uiState is AuthUiState.Success) Color.Green else Color.Red)
        }
    }
}*/