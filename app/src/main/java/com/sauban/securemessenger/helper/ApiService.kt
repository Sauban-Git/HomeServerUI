package com.sauban.securemessenger.helper

import com.sauban.securemessenger.model.Conversation
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val phoneNumber: String, val password: String, val publicKey: String)
data class SignupRequest(val phoneNumber: String, val password: String, val name: String)
data class LoginResponse(val token: String, val user: User)
data class User(val name: String, val id: String, val phoneNumber: String)
data class UserInfoResponse(val user: User)
data class SignupResponse(val user: User?, val error: String?)
data class UsersResponse(val users: List<User>)
data class UserResponse(val id: String, val name: String, val phoneNumber: String)
data class UpdateRequest(val name: String)
data class ConversationsResponse(
    val conversations: List<Conversation>
)
data class ConversationResponse(
    val conversation: Conversation
)


interface ApiService {
    @POST("user/signin")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("user/signup")
    suspend fun signup(@Body request: SignupRequest): SignupResponse

    @GET("user/info")
    suspend fun getUserInfo(): UserInfoResponse

    @GET("user")
    suspend fun getUser(): UsersResponse

    @GET("conversation/{conversationId}")
    suspend fun getConversation(
        @Path("conversationId") conversationId: String
    ): ConversationResponse
    @GET("conversation")
    suspend fun getConversations(): ConversationsResponse

//    @POST("user/update")
//    suspend fun updateProfile(@Body request: UpdateRequest): UserResponse
}
