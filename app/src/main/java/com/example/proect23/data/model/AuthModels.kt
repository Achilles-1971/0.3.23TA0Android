package com.example.proect23.data.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token_type") val tokenType: String
)

// Тело запроса для регистрации
data class RegisterRequest(
    val username: String,
    val password: String
)

// ✅ Новый класс для ответа на обновление access_token
data class TokenRefreshResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)
