package com.example.yourlife.data.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id") val id: Int,
    @SerializedName("content") val content: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_avatar") val userAvatar: String?
)

data class CreateCommentRequest(
    @SerializedName("content") val content: String
)

data class CreateCommentResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("comment") val comment: Comment
)
