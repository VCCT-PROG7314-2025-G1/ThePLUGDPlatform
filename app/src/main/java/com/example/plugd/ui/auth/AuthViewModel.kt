package com.example.plugd.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plugd.data.Repository
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: Repository) : ViewModel() {

    fun register(name: String, username: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repo.registerLocalAndRemote(name, username, email, password)
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.login(email, password)
                onResult(response.isSuccessful) // assume your api response has isSuccessful
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}