package com.example.plugd.data

import com.example.plugd.remote.ApiService
import com.example.plugd.remote.LoginRequest
import com.example.plugd.database.UserEntity
import com.example.plugd.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val api: ApiService,
    private val userDao: UserDao
) {

    // Email/password login
    suspend fun login(email: String, password: String): UserEntity = withContext(Dispatchers.IO) {
        try {
            val response = api.login(LoginRequest(email, password))
            if (!response.isSuccessful) throw Exception("Login failed")
            val body = response.body()!!
            val user = UserEntity(
                name = body.user.name,
                username = body.user.username,
                email = body.user.email,
                password = password,
                token = body.token,
                role = body.user.role ?: "User"
            )
            userDao.insertUser(user)
            user
        } catch (e: Exception) {
            // fallback offline
            userDao.getUserByEmailAndPassword(email, password) ?: throw e
        }
    }

    // Register user
    suspend fun registerLocalAndRemote(
        name: String, username: String, email: String, password: String
    ): UserEntity = withContext(Dispatchers.IO) {
        val response = api.register(mapOf(
            "name" to name, "username" to username,
            "email" to email, "password" to password
        ))
        val user = UserEntity(
            name = response.user.name,
            username = response.user.username,
            email = response.user.email,
            password = password,
            token = response.token,
            role = response.user.role ?: "User"
        )
        userDao.insertUser(user)
        user
    }

    // Biometric login: last logged-in user
    suspend fun biometricLogin(): UserEntity? = userDao.getLastLoggedInUser()

    // Firebase SSO login → exchange token with Render
    suspend fun firebaseSSOLogin(firebaseIdToken: String): UserEntity = withContext(Dispatchers.IO) {
        val response = api.loginWithFirebase(mapOf("token" to firebaseIdToken))
        val user = UserEntity(
            name = response.user.name,
            username = response.user.username,
            email = response.user.email,
            password = "SSO_LOGIN",
            token = response.token,
            role = response.user.role ?: "User"
        )
        userDao.insertUser(user)
        user
    }
}