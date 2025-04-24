package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.IndicatorValue
import retrofit2.HttpException
import java.io.IOException

class IndicatorValueRepository {
    private val api = App.indicatorValueApi

    suspend fun getAll(
        enterpriseId: Int? = null,
        indicatorId: Int? = null,
        fromDate: String? = null,
        toDate: String? = null,
        targetCurrency: String? = null,
        skip: Int? = null,
        limit: Int? = null
    ): Result<List<IndicatorValue>> = try {
        val resp = api.getAll(enterpriseId, indicatorId, fromDate, toDate, targetCurrency, skip, limit)
        if (resp.isSuccessful) {
            Result.success(resp.body()!!)
        } else {
            Result.failure(HttpException(resp))
        }
    } catch (e: IOException) {
        Result.failure(e)
    }
}
