package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.Enterprise
import retrofit2.HttpException
import java.io.IOException

class EnterpriseRepository {
    private val api = App.enterpriseApi

    suspend fun getAll(): Result<List<Enterprise>> {
        return try {
            val resp = api.getAll()
            if (resp.isSuccessful) Result.success(resp.body()!!)
            else Result.failure(HttpException(resp))
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}
