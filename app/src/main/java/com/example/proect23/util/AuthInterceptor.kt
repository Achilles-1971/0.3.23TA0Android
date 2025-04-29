package com.example.proect23.util

import com.example.proect23.App
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(
    private val prefsManager: PrefsManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = prefsManager.accessToken

        val request = if (accessToken.isNullOrBlank()) {
            originalRequest
        } else {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        }

        val response = chain.proceed(request)

        if (response.code == 401) {
            val refreshToken = prefsManager.refreshToken
            if (refreshToken.isNullOrBlank()) {
                prefsManager.clearTokens() // ✅ очищаем токены
                throw UnauthorizedException("No refresh token available")
            }

            val newAccessToken = refreshAccessToken(refreshToken)
            if (newAccessToken != null) {
                prefsManager.saveAccessToken(newAccessToken)

                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
                return chain.proceed(newRequest)
            } else {
                prefsManager.clearTokens() // ✅ очищаем токены при неудаче
                throw UnauthorizedException("Failed to refresh token")
            }
        }

        return response
    }

    private fun refreshAccessToken(refreshToken: String): String? {
        return try {
            kotlinx.coroutines.runBlocking {
                val refreshResponse = App.authApi.refresh(refreshToken)
                if (refreshResponse.isSuccessful) {
                    refreshResponse.body()?.accessToken
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

