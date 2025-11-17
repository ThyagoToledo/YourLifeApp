package com.example.yourlife.data.remote

import com.example.yourlife.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface de serviço da API - Define todos os endpoints do backend
 * Base URL: https://your-life-gamma.vercel.app/api/
 */
interface ApiService {

    // ==================== AUTENTICAÇÃO ====================

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // ==================== USUÁRIOS ====================

    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<User>

    @GET("users/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<User>

    @PUT("users/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse>

    @GET("users/search/{query}")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Path("query") query: String
    ): Response<List<User>>

    // ==================== FEED E POSTS ====================

    @GET("feed")
    suspend fun getFeed(@Header("Authorization") token: String): Response<List<Post>>

    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: CreatePostRequest
    ): Response<CreatePostResponse>

    @POST("posts/{id}/like")
    suspend fun likePost(
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    ): Response<ApiResponse>

    @DELETE("posts/{id}/like")
    suspend fun unlikePost(
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    ): Response<ApiResponse>

    @GET("posts/{id}/comments")
    suspend fun getComments(
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    ): Response<List<Comment>>

    @POST("posts/{id}/comments")
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Path("id") postId: Int,
        @Body request: CreateCommentRequest
    ): Response<CreateCommentResponse>

    // ==================== AMIGOS ====================

    @GET("friends")
    suspend fun getFriends(@Header("Authorization") token: String): Response<List<User>>

    @GET("friends/requests")
    suspend fun getFriendRequests(@Header("Authorization") token: String): Response<List<FriendRequest>>

    @GET("friends/status/{userId}")
    suspend fun getFriendshipStatus(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<FriendshipStatus>

    @POST("friends/request")
    suspend fun sendFriendRequest(
        @Header("Authorization") token: String,
        @Body request: FriendRequestData
    ): Response<ApiResponse>

    @PUT("friends/accept/{requesterId}")
    suspend fun acceptFriendRequest(
        @Header("Authorization") token: String,
        @Path("requesterId") requesterId: Int
    ): Response<ApiResponse>

    @DELETE("friends/reject/{requesterId}")
    suspend fun rejectFriendRequest(
        @Header("Authorization") token: String,
        @Path("requesterId") requesterId: Int
    ): Response<ApiResponse>

    @DELETE("friends/{id}")
    suspend fun removeFriend(
        @Header("Authorization") token: String,
        @Path("id") friendId: Int
    ): Response<ApiResponse>

    // ==================== MENSAGENS ====================

    @GET("messages/conversations")
    suspend fun getConversations(@Header("Authorization") token: String): Response<List<Conversation>>

    @GET("messages/{userId}")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<List<Message>>

    @POST("messages")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: SendMessageRequest
    ): Response<SendMessageResponse>

    @PUT("messages/{userId}/read")
    suspend fun markMessagesAsRead(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<ApiResponse>

    // ==================== NOTIFICAÇÕES ====================

    @GET("notifications")
    suspend fun getNotifications(@Header("Authorization") token: String): Response<List<Notification>>

    @PUT("notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Header("Authorization") token: String,
        @Path("id") notificationId: Int
    ): Response<ApiResponse>

    // ==================== CONSELHOS ====================

    @GET("advices")
    suspend fun getAdvices(
        @Header("Authorization") token: String,
        @Query("category") category: String? = null
    ): Response<List<Advice>>

    @POST("advices")
    suspend fun createAdvice(
        @Header("Authorization") token: String,
        @Body request: CreateAdviceRequest
    ): Response<ApiResponse>
}
