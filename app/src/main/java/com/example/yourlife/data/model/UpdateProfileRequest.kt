package com.example.yourlife.data.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("name") val name: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("cover_image") val coverImage: String? = null,
    @SerializedName("interests") val interests: List<String>? = null
)
