package com.example.plugd.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.R
import com.example.plugd.security.BiometricLogin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController? = null,
    // initial values (you can read these from Room / DataStore and pass them in)
    usernameInit: String = "EmmaJD",
    emailInit: String = "emma@example.com",
    phoneInit: String = "+27 71 234 5678",
    addressInit: String = "123 Main Street, Cape Town",
    notificationsEnabledInit: Boolean = true,
    darkModeEnabledInit: Boolean = false,
    biometricEnabledInit: Boolean = false,

    // callbacks so the host can persist / react
    onSignOut: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    onNotificationsChange: (Boolean) -> Unit = {},
    onDarkModeChange: (Boolean) -> Unit = {},
    onBiometricChange: (Boolean) -> Unit = {},
    onUpdateProfileField: (field: String, value: String) -> Unit = { _, _ -> }
) {
    // local UI state
    var username by remember { mutableStateOf(usernameInit) }
    var email by remember { mutableStateOf(emailInit) }
    var phone by remember { mutableStateOf(phoneInit) }
    var address by remember { mutableStateOf(addressInit) }

    var notificationsEnabled by remember { mutableStateOf(notificationsEnabledInit) }
    var darkModeEnabled by remember { mutableStateOf(darkModeEnabledInit) }
    var biometricEnabled by remember { mutableStateOf(biometricEnabledInit) }

    // controlling biometric prompt flow
    var showBiometricPrompt by remember { mutableStateOf(false) }

    // small dialogs for editing fields (basic)
    var editingField by remember { mutableStateOf<String?>(null) } // "username","email","phone","address","password"
    var editBuffer by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.btn_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Help button on the right
                    IconButton(onClick = {
                        navController?.navigate("about_support")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.btn_help),
                            contentDescription = "About/Support"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Account Information ---
            Text(
                text = "Account Information",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )

            SettingsItem(label = "Username", value = username) {
                editingField = "username"; editBuffer = username
            }
            SettingsItem(label = "Email", value = email) {
                editingField = "email"; editBuffer = email
            }
            SettingsItem(label = "Phone Number", value = phone) {
                editingField = "phone"; editBuffer = phone
            }
            SettingsItem(label = "Address", value = address) {
                editingField = "address"; editBuffer = address
            }
            SettingsItem(label = "Password", value = "********", actionText = "Reset") {
                // For password reset you could send an email link or open a reset screen
                editingField = "password"; editBuffer = ""
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Account Settings ---
            Text(
                text = "Account Settings",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )

            // Notifications toggle -> call callback to persist
            SettingsToggle(
                label = "Notifications",
                checked = notificationsEnabled,
                onCheckedChange = { checked ->
                    notificationsEnabled = checked
                    onNotificationsChange(checked)
                }
            )

            // Language (simple)
            SettingsItem(label = "Language", value = "English") {
                // navigate to language picker if you have one
            }

            // Dark mode toggle -> call callback so app-level theme can change
            SettingsToggle(
                label = "Dark Mode",
                checked = darkModeEnabled,
                onCheckedChange = { checked ->
                    darkModeEnabled = checked
                    onDarkModeChange(checked)
                }
            )

            // Permissions placeholders
            SettingsItem(label = "Camera & Location", actionText = "Manage") {
                // navigate to permission page / system settings intent
            }
            SettingsItem(label = "Microphone", actionText = "Manage") {
                // navigate to permission page / system settings intent
            }

            // Biometric toggle: when enabling, prompt for biometric verification first
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Biometric (Face/Touch ID)", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = if (biometricEnabled) "Enabled" else "Not enabled",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = biometricEnabled,
                    onCheckedChange = { requested ->
                        if (requested) {
                            // request biometric authentication before enabling
                            showBiometricPrompt = true
                        } else {
                            // disabling - just turn off
                            biometricEnabled = false
                            onBiometricChange(false)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Danger + signout
            Button(
                onClick = { onSignOut() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Out")
            }

            OutlinedButton(
                onClick = { onDeleteAccount() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete Account")
            }
        }
    }

    // --- Biometric prompt (composable triggers prompt when shown) ---
    if (showBiometricPrompt) {
        BiometricLogin(
            title = "Confirm to enable Biometrics",
            onSuccess = {
                biometricEnabled = true
                onBiometricChange(true)
                showBiometricPrompt = false
            },
            onFailure = {
                biometricEnabled = false
                onBiometricChange(false)
                showBiometricPrompt = false
            }
        )
    }

    // --- Edit field dialog (basic) ---
    if (editingField != null) {
        val field = editingField!!
        AlertDialog(
            onDismissRequest = { editingField = null },
            confirmButton = {
                TextButton(onClick = {
                    // update local copy & call callback
                    when (field) {
                        "username" -> { username = editBuffer; onUpdateProfileField("username", editBuffer) }
                        "email" -> { email = editBuffer; onUpdateProfileField("email", editBuffer) }
                        "phone" -> { phone = editBuffer; onUpdateProfileField("phone", editBuffer) }
                        "address" -> { address = editBuffer; onUpdateProfileField("address", editBuffer) }
                        "password" -> { /* trigger reset flow */ }
                    }
                    editingField = null
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingField = null }) { Text("Cancel") }
            },
            title = { Text("Edit ${field.replaceFirstChar { it.uppercase() }}") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editBuffer,
                        onValueChange = { editBuffer = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This is a local edit placeholder — call your API/DB in onUpdateProfileField to persist.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

/** small helper composables re-used above **/

@Composable
fun SettingsItem(label: String, value: String? = null, actionText: String = "Edit", onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            if (!value.isNullOrEmpty()) {
                Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        TextButton(onClick = onAction) { Text(actionText) }
    }
}

@Composable
fun SettingsToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}