package com.example.plugd.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.security.BiometricLogin
import com.example.plugd.ui.navigation.Routes
import com.example.plugd.ui.utils.PreviewNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun LoginScreen(navController: NavHostController) {
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
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Login button
        Button(onClick = {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            // fetch role from DB after login
                            db.child("users").child(uid).get()
                                .addOnSuccessListener { snapshot ->
                                    val role = snapshot.child("role").value as? String ?: "Unknown"
                                    navController.navigate("${Routes.HOME}?role=$role") {
                                        popUpTo(Routes.LOGIN) { inclusive = true }
                                    }
                                }
                                .addOnFailureListener {
                                    errorMessage = "Could not fetch role"
                                }
                        }
                    } else {
                        errorMessage = task.exception?.message ?: "Login failed"
                    }
                }
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Biometric login
        BiometricLogin(
            title = "Login with Biometrics",
            onSuccess = {
                // For biometrics, you’d also fetch role if you want consistency
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    db.child("users").child(uid).get()
                        .addOnSuccessListener { snapshot ->
                            val role = snapshot.child("role").value as? String ?: "Unknown"
                            navController.navigate("${Routes.HOME}?role=$role") {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                }
            },
            onFailure = {
                errorMessage = "Biometric authentication failed"
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { navController.navigate(Routes.REGISTER) }) {
            Text("Go to Register")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = PreviewNavController())
}