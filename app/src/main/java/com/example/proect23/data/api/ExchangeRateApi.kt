package com.example.proect23.data.api

import com.example.proect23.data.model.ExchangeRate
import retrofit2.Response
import retrofit2.http.*

interface ExchangeRateApi {
    @GET("exchange-rates/")
    suspend fun getAll(): Response<List<ExchangeRate>>

    @POST("exchange-rates/")
    suspend fun create(@Body e: ExchangeRate): Response<ExchangeRate>

    @PUT("exchange-rates/{id}")
    suspend fun update(@Path("id") id: Int, @Body e: ExchangeRate): Response<ExchangeRate>

    @DELETE("exchange-rates/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}
