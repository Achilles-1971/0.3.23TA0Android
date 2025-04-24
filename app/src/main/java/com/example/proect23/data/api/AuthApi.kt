package com.example.proect23.data.api

import com.example.proect23.data.model.RegisterRequest
import com.example.proect23.data.model.TokenResponse
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
}
