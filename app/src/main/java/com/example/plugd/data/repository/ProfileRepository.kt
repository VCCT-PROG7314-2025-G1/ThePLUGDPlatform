package com.example.plugd.data.repository

import android.util.Log
import com.example.plugd.data.localRoom.dao.UserProfileDao
import com.example.plugd.data.localRoom.entity.UserProfileEntity
import com.example.plugd.data.mappers.toUserProfile
import com.example.plugd.data.mappers.toUserProfileEntity
import com.example.plugd.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val profileDao: UserProfileDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val usersCollection = firestore.collection("users")

    // Listen for live updates from Firestore
    fun observeRemoteProfile(userId: String, onProfileChanged: (UserProfile?) -> Unit) {
        usersCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listen failed", error)
                    onProfileChanged(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(UserProfileEntity::class.java)?.toUserProfile()
                    onProfileChanged(user)

                    // Update local Room cache
                    user?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            profileDao.insertProfile(it.toUserProfileEntity())
                        }
                    }
                } else {
                    onProfileChanged(null)
                }
            }
    }

    // Update a single field in Firestore and local cache
    suspend fun updateProfileField(userId: String, field: String, value: String) {
        val userRef = usersCollection.document(userId)
        userRef.update(field, value).await()

        val localProfile = profileDao.getProfileById(userId)
        localProfile?.let { profile ->
            val updated = when (field) {
                "username" -> profile.copy(username = value)
                "email" -> profile.copy(email = value)
                "bio" -> profile.copy(bio = value)
                "location" -> profile.copy(location = value)
                else -> profile
            }
            profileDao.insertProfile(updated)
        }
    }

    // Get cached local profile
    suspend fun getLocalProfile(userId: String): UserProfileEntity? =
        profileDao.getProfileById(userId)

    // Fetch latest Firestore profile once
    suspend fun getRemoteProfile(userId: String): UserProfile? {
        val snapshot = usersCollection.document(userId).get().await()
        val entity = snapshot.toObject(UserProfileEntity::class.java)
        // Update local cache
        entity?.let {
            CoroutineScope(Dispatchers.IO).launch {
                profileDao.insertProfile(it)
            }
        }
        return entity?.toUserProfile()
    }
}


