package com.example.plugd.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plugd.data.repository.AuthRepository

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AuthViewModel(repository) as T
    }
}














/*package com.example.plugd.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plugd.data.localRoom.dao.UserDao
import com.example.plugd.data.repository.AuthRepository
import com.example.plugd.remote.api.RetrofitInstance.api
import com.example.plugd.remote.firebase.FirebaseAuthService
import com.google.firebase.auth.FirebaseAuth

class AuthViewModelFactory(
    private val authService: FirebaseAuthService,
    private val userDao: UserDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val repository = AuthRepository(authService, api, userDao)
            val firebaseAuth = FirebaseAuth.getInstance()
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(firebaseAuth, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/