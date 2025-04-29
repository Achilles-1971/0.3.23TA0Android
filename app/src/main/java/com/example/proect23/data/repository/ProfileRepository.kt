package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.UserProfileResponse
import com.example.proect23.util.UnauthorizedException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException

class ProfileRepository {
    private val api = App.authApi

    suspend fun loadProfile(): Result<UserProfileResponse> {
        return try {
            val resp = api.getCurrentUser()
            handleResponse(resp)
        } catch (e: IOException) {
            Result.failure(Exception("Проверьте подключение к интернету"))
        }
    }

    suspend fun uploadAvatar(file: File): Result<UserProfileResponse> {
        return try {
            val mimeType = when (file.extension.lowercase()) {
                "png" -> "image/png"
                "jpg", "jpeg" -> "image/jpeg"
                else -> throw IllegalArgumentException("Unsupported image type: ${file.extension}")
            }

            val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())

            val body = MultipartBody.Part.createFormData(
                name = "file",
                filename = file.name,
                body = requestFile
            )

            val resp = api.uploadAvatar(body)
            handleResponse(resp)
        } catch (e: IOException) {
            Result.failure(Exception("Проверьте подключение к интернету"))
        } catch (e: IllegalArgumentException) {
            Result.failure(Exception(e.message ?: "Неподдерживаемый формат файла"))
        }
    }

    private fun <T> handleResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            when (response.code()) {
                401 -> Result.failure(UnauthorizedException())
                400 -> {
                    val errorMessage = parseErrorMessage(response)
                    Result.failure(Exception(errorMessage))
                }
                else -> Result.failure(Exception("Ошибка ${response.code()} при загрузке"))
            }
        }
    }

    private fun parseErrorMessage(response: Response<*>): String {
        return try {
            response.errorBody()?.string()?.let { errorBody ->
                val json = JSONObject(errorBody)
                json.getString("detail")
            } ?: "Неизвестная ошибка"
        } catch (e: Exception) {
            "Неизвестная ошибка"
        }
    }
}
