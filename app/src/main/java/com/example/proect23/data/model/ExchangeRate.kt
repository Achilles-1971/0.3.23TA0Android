package com.example.proect23.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeRate(
    val id: Int,
    @SerializedName("from_currency") val fromCurrency: String,
    @SerializedName("to_currency")   val toCurrency: String,
    val rate: Double,
    @SerializedName("rate_date")     val rateDate: String // yyyy-MM-dd
)
