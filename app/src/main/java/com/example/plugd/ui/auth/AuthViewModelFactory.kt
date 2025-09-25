package com.example.plugd.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plugd.database.UserDao
import com.example.plugd.remote.firebase.FirebaseAuthService

class AuthViewModelFactory(private val authService: FirebaseAuthService, private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AuthViewModel(authService, userDao) as T
    }
}