package com.example.proect23.data.api

import com.example.proect23.data.model.Indicator
import retrofit2.Response
import retrofit2.http.*

interface IndicatorApi {
    @GET("indicators/")
    suspend fun getAll(): Response<List<Indicator>>

    @POST("indicators/")
    suspend fun create(@Body indicator: Indicator): Response<Indicator>

    @PUT("indicators/{id}")
    suspend fun update(@Path("id") id: Int, @Body indicator: Indicator): Response<Indicator>

    @DELETE("indicators/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}
