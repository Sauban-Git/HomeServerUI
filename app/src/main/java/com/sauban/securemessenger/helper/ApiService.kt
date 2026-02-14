package com.sauban.securemessenger.helper

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val phoneNumber: String, val password: String)
data class SignupRequest(val phoneNumber: String, val password: String, val name: String)
data class LoginResponse(val token: String, val user: User)
data class User(val name: String, val id: String, val phoneNumber: String)
data class UserInfoResponse(val name: String, val id: String, val phoneNumber: String)
data class SignupResponse(val user: User)
data class UserResponse(val id: String, val name: String, val phoneNumber: String)
data class ConversationResponse(val id: String, val messages: List<String>)
data class UpdateRequest(val name: String)

interface ApiService {
    @POST("user/signin")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("user/signup")
    suspend fun signup(@Body request: SignupRequest): SignupResponse

    @GET("user/info")
    suspend fun getUserInfo(): UserInfoResponse

    @GET("user")
    suspend fun getUser(): UserResponse

    @GET("conversation")
    suspend fun getConversations(): List<ConversationResponse>

//    @POST("user/update")
//    suspend fun updateProfile(@Body request: UpdateRequest): UserResponse
}
