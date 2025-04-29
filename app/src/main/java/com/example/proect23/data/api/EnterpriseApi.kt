package com.example.proect23.data.api

import com.example.proect23.data.model.Enterprise
import com.example.proect23.data.model.EnterpriseCreateRequest
import com.example.proect23.data.model.EnterpriseUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface EnterpriseApi {
    @GET("enterprises/")
    suspend fun getAll(): Response<List<Enterprise>>

    @POST("enterprises/")
    suspend fun create(@Body body: EnterpriseCreateRequest): Response<Enterprise>

    @PUT("enterprises/{id}")
    suspend fun update(@Path("id") id: Int, @Body body: EnterpriseUpdateRequest): Response<Enterprise>

    @DELETE("enterprises/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}
