package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.RegisterRequest
import com.example.proect23.data.model.TokenResponse
import retrofit2.HttpException
import java.io.IOException

class UserRepository {
    private val authApi = App.authApi

    /** Попытка регистрации */
    suspend fun register(username: String, password: String): Result<TokenResponse> {
        return try {
            val response = authApi.register(RegisterRequest(username, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    /** Попытка логина */
    suspend fun login(username: String, password: String): Result<TokenResponse> {
        return try {
            val response = authApi.login(username, password)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}
