package com.example.proect23.data.model

// Ответ от сервера при регистрации/логине
data class TokenResponse(
    val access_token: String,
    val token_type: String
)

// Тело запроса для регистрации
data class RegisterRequest(
    val username: String,
    val password: String
)
