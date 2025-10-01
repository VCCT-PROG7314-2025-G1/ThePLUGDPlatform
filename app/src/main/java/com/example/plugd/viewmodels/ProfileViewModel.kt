package com.example.plugd.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plugd.data.localRoom.entity.UserEntity
import com.example.plugd.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadUser(uid: String?) {
        if (uid == null) {
            _error.value = "No logged-in user"
            _loading.value = false
            return
        }

        fun updateUser(user: UserEntity) {
            viewModelScope.launch {
                try {
                    userRepository.updateUserProfile(user)  // updates Room + Firestore
                    _user.value = user  // update state
                } catch (e: Exception) {
                    _error.value = e.message ?: "Failed to update profile"
                }
            }
        }

        viewModelScope.launch {
            _loading.value = true

            try {
                // 1️⃣ Try remote first
                val remoteUser = userRepository.fetchUserFromRemote(uid)
                if (remoteUser != null) {
                    _user.value = remoteUser
                    // Save to local Room for caching
                    userRepository.updateUserProfile(remoteUser)
                } else {
                    // 2️⃣ Try local DB as fallback
                    val localUser = userRepository.getUserById(uid)
                    if (localUser != null) {
                        _user.value = localUser
                    } else {
                        _error.value = "User not found"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load user"
            }

            _loading.value = false
        }
    }
}