package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.WeightedIndicator
import retrofit2.HttpException
import java.io.IOException

class WeightedIndicatorRepository {
    private val api = App.weightedIndicatorApi

    suspend fun getAll(
        enterpriseId: Int,
        indicatorId: Int? = null,
        fromDate: String? = null,
        toDate: String? = null,
        targetCurrency: String? = null,
        aggregate: Boolean = false,
        groupBy: String? = null,
        skip: Int? = null,
        limit: Int? = null
    ): Result<List<WeightedIndicator>> = try {
        val resp = api.getAll(
            enterpriseId, indicatorId, fromDate, toDate,
            targetCurrency, aggregate, groupBy, skip, limit
        )
        if (resp.isSuccessful) Result.success(resp.body()!!)
        else Result.failure(HttpException(resp))
    } catch(e: IOException) {
        Result.failure(e)
    }
}
