package com.example.plugd.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plugd.database.UserDao
import com.example.plugd.database.UserEntity
import com.example.plugd.remote.firebase.FirebaseAuthService
import kotlinx.coroutines.launch

class AuthViewModel(private val authService: FirebaseAuthService, private val userDao: UserDao) : ViewModel() {
    fun loginWithEmail(email: String, password: String, onResult: (Result<UserEntity>) -> Unit) {
        viewModelScope.launch {
            try {
                val firebaseUser = authService.loginUser(email, password)
                if (firebaseUser != null) {
                    // in practice fetch remote user and insert local (omitted for brevity)
                    val local = userDao.getUserByEmailAndPassword(email, password)
                    if (local != null) onResult(Result.success(local)) else onResult(Result.failure(Exception("No local user")))
                } else {
                    val local = userDao.getUserByEmailAndPassword(email, password)
                    if (local != null) onResult(Result.success(local)) else onResult(Result.failure(Exception("Login failed")))
                }
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }
}
