package com.example.plugd.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response

data class LoginRequest(val email: String, val password: String)
data class UserDto(val id: Int, val username: String, val name: String, val email: String)
data class TokenResponse(val token: String, val user: UserDto)
data class EventDto(val id: Int, val title: String, val description: String?)

interface ApiService {
    @GET("api/events")
    suspend fun getEvents(): List<EventDto>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<TokenResponse>

    @POST("api/auth/register")
    suspend fun register(@Body body: Map<String, String>): TokenResponse
}