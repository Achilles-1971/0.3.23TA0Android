package com.example.proect23.data.model

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val id: Int,
    val username: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)
