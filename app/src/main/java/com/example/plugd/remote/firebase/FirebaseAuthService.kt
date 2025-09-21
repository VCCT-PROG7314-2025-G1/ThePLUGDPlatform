package com.example.plugd.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Register user with Firebase Authentication
    suspend fun registerUser(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    // Login user
    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    // Logout user
    fun logout() {
        auth.signOut()
    }

    // Get currently logged-in user
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}