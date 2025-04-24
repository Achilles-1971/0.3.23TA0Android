package com.example.proect23.data.api

import com.example.proect23.data.model.IndicatorValue
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IndicatorValueApi {
    @GET("indicator-values/")
    suspend fun getAll(
        @Query("enterprise_id") enterpriseId: Int? = null,
        @Query("indicator_id") indicatorId: Int? = null,
        @Query("from_date") fromDate: String? = null,
        @Query("to_date") toDate: String? = null,
        @Query("target_currency") targetCurrency: String? = null,
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<List<IndicatorValue>>
}
