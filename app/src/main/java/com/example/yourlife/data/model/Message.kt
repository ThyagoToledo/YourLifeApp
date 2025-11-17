package com.example.yourlife.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de dados para Mensagens
 */
data class Message(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("from_user_id")
    val fromUserId: Int,
    
    @SerializedName("to_user_id")
    val toUserId: Int,
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("is_read")
    val isRead: Boolean = false,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("sender_name")
    val senderName: String? = null,
    
    @SerializedName("sender_avatar")
    val senderAvatar: String? = null
)

/**
 * Modelo para lista de conversas
 */
data class Conversation(
    @SerializedName("friend_id")
    val friendId: Int,

    @SerializedName("friend_name")
    val friendName: String,

    @SerializedName("friend_avatar")
    val friendAvatar: String? = null,

    @SerializedName("last_message")
    val lastMessage: String? = null,

    @SerializedName("unread_count")
    val unreadCount: Int = 0,

    @SerializedName("last_message_time")
    val lastMessageTime: String? = null
)

/**
 * Request para enviar mensagem
 */
data class SendMessageRequest(
    @SerializedName("to_user_id")
    val toUserId: Int,

    @SerializedName("content")
    val content: String
)

/**
 * Response ao enviar mensagem
 */
data class SendMessageResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: Message? = null,

    @SerializedName("error")
    val error: String? = null
)

/**
 * Modelo de dados para Notificações
 */
data class Notification(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("related_user_id")
    val relatedUserId: Int? = null,

    @SerializedName("is_read")
    val isRead: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String
)

/**
 * Response genérica de sucesso
 */
data class ApiResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("error")
    val error: String? = null
)

