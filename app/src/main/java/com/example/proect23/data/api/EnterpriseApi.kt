package com.example.proect23.data.api

import com.example.proect23.data.model.Enterprise
import retrofit2.Response
import retrofit2.http.*

interface EnterpriseApi {
    @GET("enterprises/")
    suspend fun getAll(): Response<List<Enterprise>>

    @POST("enterprises/")
    suspend fun create(@Body body: Enterprise): Response<Enterprise>

    @PUT("enterprises/{id}")
    suspend fun update(@Path("id") id: Int, @Body body: Enterprise): Response<Enterprise>

    @DELETE("enterprises/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}
