package com.example.yourlife.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Modelo de dados para Usuário
 * Corresponde à tabela 'users' no backend PostgreSQL
 */
@Parcelize
data class User(
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

    @SerializedName("cover_image")
    val coverImage: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("interests")
    val interests: List<String>? = null,

    @SerializedName("posts_count")
    val postsCount: Int? = null,

    @SerializedName("friends_count")
    val friendsCount: Int? = null
) : Parcelable

/**
 * Request para login
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

/**
 * Request para registro
 */
data class RegisterRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

/**
 * Response de autenticação (login/registro)
 */
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("error")
    val error: String? = null
)
