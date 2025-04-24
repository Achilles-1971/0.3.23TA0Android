package com.example.proect23.data.model

import com.google.gson.annotations.SerializedName

data class IndicatorValue(
    val id: Int,

    @SerializedName("enterprise_id")
    val enterpriseId: Int,

    @SerializedName("indicator_id")
    val indicatorId: Int,

    @SerializedName("value_date")
    val valueDate: String,          // формат "yyyy-MM-dd"

    val value: Double,

    @SerializedName("currency_code")
    val currencyCode: String,

    @SerializedName("converted_value")
    val convertedValue: Double?,    // может быть null, если нет курса

    val warning: String?            // сообщение-предупреждение
)
