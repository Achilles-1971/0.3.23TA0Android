package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.Indicator
import retrofit2.HttpException
import java.io.IOException

class IndicatorRepository {
    private val api = App.indicatorApi

    suspend fun getAll(): Result<List<Indicator>> = try {
        val resp = api.getAll()
        if (resp.isSuccessful) Result.success(resp.body()!!)
        else Result.failure(HttpException(resp))
    } catch(e: IOException) {
        Result.failure(e)
    }
}
