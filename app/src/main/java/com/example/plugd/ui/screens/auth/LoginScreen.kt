package com.example.plugd.ui.screens.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    // JUST ADDED
    navController: NavHostController,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()

    // Google Sign In client
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    // States for email & password
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Launcher for Google Sign-In intent
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d("LoginScreen", "firebaseAuthWithGoogle:" + account.id)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginScreen", "Google sign in success")
                        onLoginSuccess()
                    } else {
                        Log.w("LoginScreen", "Google sign in failed", task.exception)
                    }
                }
        } catch (e: ApiException) {
            Log.w("LoginScreen", "Google sign in failed", e)
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Normal Login button
            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                Log.d("LoginScreen", "Email login success")
                                onLoginSuccess()
                            } else {
                                Log.w("LoginScreen", "Email login failed", task.exception)
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Google Sign-In button
            OutlinedButton(
                onClick = {
                    val signInIntent: Intent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign in with Google")
            }
        }
    }
}