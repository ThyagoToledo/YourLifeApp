package com.example.yourlife.data.model

import com.google.gson.annotations.SerializedName

/**
 * Status da amizade
 */
data class FriendshipStatus(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("status")
    val status: String, // "none", "pending", "accepted"

    @SerializedName("isSender")
    val isSender: Boolean = false
)

/**
 * Request de pedido de amizade
 */
data class FriendRequestData(
    @SerializedName("friend_id")
    val friendId: Int
)

/**
 * Pedido de amizade recebido
 */
data class FriendRequest(
    @SerializedName("request_id")
    val requestId: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("avatar")
    val avatar: String? = null,

    @SerializedName("bio")
    val bio: String? = null,

    @SerializedName("created_at")
    val createdAt: String
)

/**
 * Modelo de dados para Conselhos
 */
data class Advice(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("author_id")
    val authorId: Int,

    @SerializedName("author_name")
    val authorName: String? = null,

    @SerializedName("created_at")
    val createdAt: String
)

/**
 * Request para criar conselho
 */
data class CreateAdviceRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("category")
    val category: String = "geral"
)

