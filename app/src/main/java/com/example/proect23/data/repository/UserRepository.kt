package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.RegisterRequest
import com.example.proect23.data.model.TokenResponse
import com.example.proect23.data.model.UserProfileResponse
import com.example.proect23.util.PrefsManager
import com.example.proect23.util.UnauthorizedException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class UserRepository {
    private val authApi = App.authApi

    suspend fun register(username: String, password: String): Result<TokenResponse> {
        return try {
            val response = authApi.register(RegisterRequest(username, password))
            if (response.isSuccessful) {
                val tokenResponse = response.body()!!
                PrefsManager.token = tokenResponse.accessToken
                PrefsManager.refreshToken = tokenResponse.refreshToken
                Result.success(tokenResponse)
            } else {
                handleHttpError(response.code(), response)
            }
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, password: String): Result<TokenResponse> {
        return try {
            val response = authApi.login(username, password)
            if (response.isSuccessful) {
                val tokenResponse = response.body()!!
                PrefsManager.token = tokenResponse.accessToken
                PrefsManager.refreshToken = tokenResponse.refreshToken
                Result.success(tokenResponse)
            } else {
                handleHttpError(response.code(), response)
            }
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    suspend fun uploadAvatar(file: File): Result<UserProfileResponse> {
        return try {
            // Определяем точный MIME-тип по расширению
            val mimeType = when (file.extension.lowercase()) {
                "png" -> "image/png"
                "jpg", "jpeg" -> "image/jpeg"
                else -> throw IllegalArgumentException("Unsupported image type: ${file.extension}")
            }
            val requestBody = file
                .asRequestBody(mimeType.toMediaTypeOrNull())

            val part = MultipartBody.Part.createFormData(
                name = "file",
                filename = file.name,
                body = requestBody
            )

            val response = authApi.uploadAvatar(part)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                handleHttpError(response.code(), response)
            }
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: IOException) {
            Result.failure(Exception("Проверьте подключение к интернету"))
        }
    }

    private fun <T> handleHttpError(code: Int, response: retrofit2.Response<T>): Result<T> {
        return when (code) {
            401 -> throw UnauthorizedException()
            400 -> {
                Result.failure(Exception("Only JPEG or PNG images are allowed"))
            }
            else -> Result.failure(HttpException(response))
        }
    }
}
