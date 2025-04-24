package com.example.proect23.data.api

import com.example.proect23.data.model.WeightedIndicator
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeightedIndicatorApi {
    @GET("weighted-indicators/")
    suspend fun getAll(
        @Query("enterprise_id") enterpriseId: Int,
        @Query("indicator_id") indicatorId: Int? = null,
        @Query("from_date") fromDate: String? = null,
        @Query("to_date") toDate: String? = null,
        @Query("target_currency") targetCurrency: String? = null,
        @Query("aggregate") aggregate: Boolean = false,
        @Query("group_by") groupBy: String? = null,
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<List<WeightedIndicator>>
}
