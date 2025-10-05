package com.example.plugd.data.remoteFireStore

import com.example.plugd.data.localRoom.entity.UserProfileEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
class UserRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val userCollection = firestore.collection("users")

    suspend fun uploadUser(user: UserProfileEntity) {
        userCollection.document(user.userId).set(user).await()
    }

    suspend fun fetchAllUsers(): List<UserProfileEntity> {
        val snapshot = userCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(UserProfileEntity::class.java) }
    }

    suspend fun fetchUserById(userId: String): UserProfileEntity? {
        return userCollection.document(userId).get().await()
            .toObject(UserProfileEntity::class.java)
    }
}





/* Cloud access layer (Firebase Firestore)
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
}*/