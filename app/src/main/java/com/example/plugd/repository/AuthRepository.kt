package com.example.plugd.repository

import com.example.plugd.data.localRoom.dao.UserDao
import com.example.plugd.data.localRoom.entity.UserEntity
import com.example.plugd.remote.api.ApiService
import com.example.plugd.remote.api.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val api: ApiService,
    private val userDao: UserDao
) {

    // Email/password login
    suspend fun login(email: String, password: String): UserEntity = withContext(Dispatchers.IO) {
        try {
            val response = api.login(LoginRequest(email, password))
            if (!response.isSuccessful) throw Exception("Login failed")
            val body = response.body()!!

            // Convert backend Int ID to String
            val userId = body.user.id.toString()

            val user = UserEntity(
                userId = userId,
                name = body.user.name,
                username = body.user.username,
                email = body.user.email,
                password = password,  // store for offline login
                role = body.user.role ?: "User",
            )

            userDao.insertUser(user)
            user
        } catch (e: Exception) {
            // fallback offline login
            userDao.getUserByEmailAndPassword(email, password) ?: throw e
        }
    }

    // Register user (local + remote)
    suspend fun registerLocalAndRemote(
        name: String,
        username: String,
        email: String,
        password: String
    ): UserEntity = withContext(Dispatchers.IO) {
        val response = api.register(
            mapOf(
                "name" to name,
                "username" to username,
                "email" to email,
                "password" to password
            )
        )

        val userId = response.user.id.toString()

        val user = UserEntity(
            userId = userId,
            name = response.user.name,
            username = response.user.username,
            email = response.user.email,
            password = password,  // store locally
            role = response.user.role ?: "User",
        )

        userDao.insertUser(user)
        user
    }

    // Biometric login: get last logged-in user from Room
    suspend fun biometricLogin(): UserEntity? = userDao.getLastLoggedInUser()

    // Firebase SSO login → exchange token with backend
    suspend fun firebaseSSOLogin(firebaseIdToken: String): UserEntity = withContext(Dispatchers.IO) {
        val response = api.loginWithFirebase(mapOf("token" to firebaseIdToken))

        val userId = response.user.id.toString()

        val user = UserEntity(
            userId = userId,
            name = response.user.name,
            username = response.user.username,
            email = response.user.email,
            password = "SSO_LOGIN",  // marker for SSO
            role = response.user.role ?: "User",
        )

        userDao.insertUser(user)
        user
    }
}