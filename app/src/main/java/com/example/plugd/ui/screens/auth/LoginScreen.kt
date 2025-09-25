package com.example.plugd.ui.screens.auth

import android.content.IntentSender
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.ui.auth.GoogleAuthUiClient
import com.example.plugd.ui.screens.nav.BottomNavBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.plugd.ui.navigation.Routes
import android.app.Activity

@Composable
fun LoginScreen(
    navController: NavHostController,
    firebaseAuth: FirebaseAuth? = null,
    googleAuthClient: GoogleAuthUiClient,
    biometricLogin: (() -> Unit)? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auth = firebaseAuth ?: FirebaseAuth.getInstance()

    // 🔑 Place the launcher here (after scope and before UI)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                scope.launch {
                    val signInResult = googleAuthClient.signInWithIntent(intent)
                    if (signInResult.data != null) {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        errorMessage = signInResult.errorMessage ?: "Google Sign-in failed"
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Email/Password
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Login button
        Button(onClick = {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        errorMessage = task.exception?.message ?: "Login failed"
                    }
                }
        }) { Text("Login") }

        Spacer(modifier = Modifier.height(8.dp))

        // Biometric login
        biometricLogin?.let { Button(onClick = it) { Text("Login with Biometrics") } }

        Spacer(modifier = Modifier.height(8.dp))

        // Google Sign-In
        // Google Sign-In UI
        Text("Or sign in with Google", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    try {
                        val intentSender = googleAuthClient.signIn() // use the object directly
                        intentSender?.let {
                            launcher.launch(IntentSenderRequest.Builder(it).build())
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Google Sign-in failed"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google")
        }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }