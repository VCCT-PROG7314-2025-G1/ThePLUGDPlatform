package com.example.plugd.ui.screens.auth

import GoogleAuthUiClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.ui.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterScreen(
    navController: NavHostController,
    //role: String,
    //googleAuthUiClient: GoogleAuthUiClient
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseDatabase.getInstance().reference

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid
                                if (uid != null) {
                                    db.child("users").child(uid).setValue(
                                        mapOf(
                                            "name" to name,
                                            "username" to username,
                                            "email" to email,
                                            "role" to "User" // default role
                                        )
                                    )
                                }
                                navController.navigate(Routes.LOGIN) {
                                    popUpTo(Routes.REGISTER) { inclusive = true }
                                }
                            } else {
                                errorMessage = task.exception?.message ?: "Registration failed"
                            }
                        }
                } else {
                    errorMessage = "Please fill all fields"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Already have an account? Login
        Text(
            text = "Already have an account? Login",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.REGISTER) { inclusive = true }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}