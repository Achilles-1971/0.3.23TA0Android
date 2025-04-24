package com.example.proect23.data.model

import com.google.gson.annotations.SerializedName

data class Indicator(
    val id: Int,
    val name: String,
    val importance: Double,
    val unit: String
)
