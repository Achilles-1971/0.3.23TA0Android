package com.example.proect23.data.api

import com.example.proect23.data.model.Currency
import retrofit2.Response
import retrofit2.http.*

interface CurrencyApi {
    @GET("currencies/")
    suspend fun getAll(): Response<List<Currency>>

    @POST("currencies/")
    suspend fun create(@Body currency: Currency): Response<Currency>

    @PUT("currencies/{code}")
    suspend fun update(@Path("code") code: String, @Body currency: Currency): Response<Currency>

    @DELETE("currencies/{code}")
    suspend fun delete(@Path("code") code: String): Response<Unit>
}
