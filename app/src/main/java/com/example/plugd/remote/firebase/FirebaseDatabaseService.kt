package com.example.plugd.remote.firebase

import com.example.plugd.model.User
import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabaseService {

    // Corrected syntax
    private val db = FirebaseDatabase.getInstance("https://plugdapp-default-rtdb.firebaseio.com/").reference

    fun saveUser(user: User) {
        db.child("users").child(user.userId).setValue(user)
            .addOnSuccessListener {
                // Successfully saved
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    fun getUser(userId: String, onResult: (User?) -> Unit) {
        db.child("users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}