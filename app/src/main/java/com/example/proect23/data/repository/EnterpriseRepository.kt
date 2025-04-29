package com.example.proect23.data.repository

import com.example.proect23.App
import com.example.proect23.data.model.Enterprise
import com.example.proect23.data.model.EnterpriseCreateRequest
import com.example.proect23.data.model.EnterpriseUpdateRequest
import retrofit2.HttpException
import java.io.IOException

class EnterpriseRepository {
    private val api = App.enterpriseApi

    /** Получить список всех предприятий */
    suspend fun getAll(): Result<List<Enterprise>> {          // ← блок
        return try {
            val resp = api.getAll()
            if (resp.isSuccessful) Result.success(resp.body()!!)
            else Result.failure(HttpException(resp))
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    // ─── НОВАЯ функция — можно оставить expression-body ───
    suspend fun getAllSortedSafe(): List<Enterprise> =
        getAll()
            .getOrDefault(emptyList())
            .sortedBy { it.name.lowercase() }

    /** Получить конкретное предприятие по ID */
    suspend fun getById(id: Int): Result<Enterprise> {        // ← блок
        return try {
            val resp = api.getAll()
            if (!resp.isSuccessful) return Result.failure(HttpException(resp))
            val item = resp.body()!!.find { it.id == id }
                ?: return Result.failure(NoSuchElementException("id=$id not found"))
            Result.success(item)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    /** Обновить предприятие */
    suspend fun update(enterprise: Enterprise): Result<Enterprise> {   // ← блок
        return try {
            val body = EnterpriseUpdateRequest(
                name           = enterprise.name,
                requisites     = enterprise.requisites,
                phone          = enterprise.phone,
                contact_person = enterprise.contact_person
            )
            val resp = api.update(enterprise.id, body)
            if (resp.isSuccessful) Result.success(resp.body()!!)
            else Result.failure(HttpException(resp))
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    /** Удалить предприятие */
    suspend fun delete(id: Int): Result<Unit> {               // ← блок
        return try {
            val resp = api.delete(id)
            if (resp.isSuccessful) Result.success(Unit)
            else Result.failure(HttpException(resp))
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    /** Создать предприятие */
    suspend fun create(enterprise: Enterprise): Result<Enterprise> {   // ← блок
        return try {
            val body = EnterpriseCreateRequest(
                name           = enterprise.name,
                requisites     = enterprise.requisites,
                phone          = enterprise.phone,
                contact_person = enterprise.contact_person
            )
            val resp = api.create(body)
            if (resp.isSuccessful) Result.success(resp.body()!!)
            else Result.failure(HttpException(resp))
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}
