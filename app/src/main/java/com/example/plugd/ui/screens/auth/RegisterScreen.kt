package com.example.plugd.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.ui.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.plugd.ui.utils.PreviewNavController

@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
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

        Button(onClick = {
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
                                    "role" to "Artist" // or whatever the chosen role is
                                )
                            )
                        }

                        // Navigate with role
                        val userRole = "Artist" // replace with selected role
                        navController.navigate("${Routes.HOME}?role=$userRole") {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    } else {
                        errorMessage = task.exception?.message ?: "Registration failed"
                    }
                }
        }) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { navController.navigate(Routes.LOGIN) }) {
            Text("Go to Login")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = errorMessage, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
        }

    }
}

// Helper for preview
@Composable
fun rememberNavControllerPreview(): NavHostController {
    return androidx.navigation.compose.rememberNavController()
}

// Preview function (outside of RegisterScreen)
@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navController = rememberNavControllerPreview())
}