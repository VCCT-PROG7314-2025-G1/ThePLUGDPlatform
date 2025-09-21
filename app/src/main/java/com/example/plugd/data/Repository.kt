package com.example.plugd.data

import com.example.plugd.remote.ApiService
import com.example.plugd.remote.LoginRequest
import com.example.plugd.database.UserEntity
import com.example.plugd.App
import com.example.plugd.remote.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository(private val api: ApiService) {
    private val userDao = App.database.userDao()

    suspend fun registerLocalAndRemote(name: String, username: String, email: String, password: String) {
        // Create local entity for offline mode
        withContext(Dispatchers.IO) {
            userDao.insertUser(UserEntity(name = name, username = username, email = email, password = password))
        }
        // call remote register
        api.register(mapOf("name" to name, "username" to username, "email" to email, "password" to password))
    }

    suspend fun login(email: String, password: String): Response<TokenResponse> {
        return api.login(LoginRequest(email, password))
    }
}