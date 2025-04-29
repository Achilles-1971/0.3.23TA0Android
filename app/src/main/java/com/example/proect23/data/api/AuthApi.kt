package com.example.proect23.data.api

import com.example.proect23.data.model.RegisterRequest
import com.example.proect23.data.model.TokenResponse
import com.example.proect23.data.model.UserProfileResponse
import com.example.proect23.data.model.TokenRefreshResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("register")
    suspend fun register(
        @Body body: RegisterRequest
    ): Response<TokenResponse>

    @FormUrlEncoded
    @POST("token")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<TokenResponse>

    @GET("users/me")
    suspend fun getCurrentUser(): Response<UserProfileResponse>

    @Multipart
    @POST("users/me/avatar")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): Response<UserProfileResponse>

    @POST("refresh")
    suspend fun refresh(
        @Query("refresh_token") refreshToken: String
    ): Response<TokenRefreshResponse>
}
