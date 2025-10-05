package com.example.plugd.ui.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {

    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun getUserDocument(userId: String? = currentUserId()): DocumentReference {
        val safeId = userId ?: throw IllegalStateException("User not logged in")
        return FirebaseFirestore.getInstance().collection("users").document(safeId)
    }
}
