package com.example.plugd.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plugd.data.mappers.toUserProfile
import com.example.plugd.data.repository.AuthRepository
import com.example.plugd.data.repository.ProfileRepository
import com.example.plugd.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    //just added
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /** Load profile: cached first, then Firestore, then live updates */
    fun loadProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            _error.value = "User not logged in"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                // Load cached Room profile
                val local = profileRepository.getLocalProfile(userId)
                if (local != null) _profile.value = local.toUserProfile()

                // Fetch latest from Firestore once
                val remote = profileRepository.getRemoteProfile(userId)
                if (remote != null) _profile.value = remote

                // Listen for live updates
                profileRepository.observeRemoteProfile(userId) { liveUser ->
                    if (liveUser != null) _profile.value = liveUser
                }

                if (_profile.value == null) _error.value = "Profile not found"
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load profile"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateProfileField(field: String, value: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                profileRepository.updateProfileField(userId, field, value)
                _profile.value = _profile.value?.let { current ->
                    when (field) {
                        "username" -> current.copy(username = value)
                        "email" -> current.copy(email = value)
                        "bio" -> current.copy(bio = value)
                        "location" -> current.copy(location = value)
                        else -> current
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update profile"
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onComplete()
        }
    }
}

