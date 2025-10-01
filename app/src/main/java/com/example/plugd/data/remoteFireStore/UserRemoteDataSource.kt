package com.example.plugd.data.remoteFireStore

import com.example.plugd.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Cloud access layer (Firebase Firestore)
class UserRemoteDataSource {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun getUser(userId: String): UserProfile? {
        return try {
            val snapshot = usersCollection.document(userId).get().await()
            snapshot.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateUser(userProfile: UserProfile) {
        try {
            usersCollection.document(userProfile.userId).set(userProfile).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun observeUser(userId: String, onChange: (UserProfile?) -> Unit) {
        usersCollection.document(userId)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObject(UserProfile::class.java)
                onChange(user)
            }
    }
}