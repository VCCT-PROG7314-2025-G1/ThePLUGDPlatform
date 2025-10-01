package com.example.plugd.remote.firebase

import com.google.firebase.database.FirebaseDatabase
import com.example.plugd.model.UserProfile

class FirebaseDatabaseService {

    // Corrected syntax
    private val db = FirebaseDatabase.getInstance("https://plugdapp-default-rtdb.firebaseio.com/").reference

    fun saveUser(user: UserProfile) {
        db.child("users").child(user.userId).setValue(user)
            .addOnSuccessListener { }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    fun getUser(userId: String, onResult: (UserProfile?) -> Unit) {
        db.child("users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(UserProfile::class.java)
                onResult(user)
            }
            .addOnFailureListener { onResult(null) }
    }
}