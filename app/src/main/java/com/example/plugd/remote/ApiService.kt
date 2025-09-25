package com.example.plugd.remote

import com.example.plugd.data.EventEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response

data class LoginRequest(val email: String, val password: String)
data class UserDto(val id: Int, val username: String, val name: String, val email: String, val role: String?)
data class TokenResponse(val token: String, val user: UserDto)
data class EventDto(val id: Int, val title: String, val description: String?)

interface ApiService {
    @GET("api/events")
    suspend fun getEvents(): List<EventEntity>  // use EventEntity directly

    @POST("events")
    suspend fun addEvent(@Body event: EventEntity)

    // Auth endpoints
    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<TokenResponse>

    @POST("api/auth/register")
    suspend fun register(@Body body: Map<String, String>): TokenResponse

    @POST("api/auth/firebase")
    suspend fun loginWithFirebase(@Body body: Map<String, String>): TokenResponse
}