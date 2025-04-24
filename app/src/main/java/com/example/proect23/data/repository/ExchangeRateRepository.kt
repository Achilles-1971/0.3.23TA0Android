package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.ExchangeRate
import retrofit2.HttpException
import java.io.IOException

class ExchangeRateRepository {
    private val api = App.exchangeRateApi

    suspend fun getAll(): Result<List<ExchangeRate>> = try {
        val resp = api.getAll()
        if (resp.isSuccessful) Result.success(resp.body()!!)
        else Result.failure(HttpException(resp))
    } catch (e: IOException) {
        Result.failure(e)
    }
}
