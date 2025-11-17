package com.example.yourlife.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") val id: Int,
    @SerializedName("content") val content: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_avatar") val userAvatar: String?,
    @SerializedName("likes_count") val likesCount: Int,
    @SerializedName("comments_count") val commentsCount: Int,
    @SerializedName("is_liked") val isLiked: Boolean,
    @SerializedName("is_advice") val isAdvice: Boolean
)

data class CreatePostRequest(
    @SerializedName("content") val content: String
)

data class CreatePostResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("post") val post: Post
)
