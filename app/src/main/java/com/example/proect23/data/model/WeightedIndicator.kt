package com.example.proect23.data.model

import com.google.gson.annotations.SerializedName

data class WeightedIndicator(
    @SerializedName("indicator_id")
    val indicatorId: Int,

    @SerializedName("indicator_name")
    val indicatorName: String,

    @SerializedName("value_date")
    val valueDate: String,

    @SerializedName("original_value")
    val originalValue: Double,

    @SerializedName("currency_code")
    val currencyCode: String,

    val importance: Double,

    @SerializedName("weighted_value")
    val weightedValue: Double,

    @SerializedName("converted_weighted_value")
    val convertedWeightedValue: Double?,

    val warning: String?
)
