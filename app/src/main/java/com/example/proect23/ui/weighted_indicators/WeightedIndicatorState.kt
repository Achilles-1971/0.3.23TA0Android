package com.example.proect23.ui.weighted_indicators

import com.example.proect23.data.model.WeightedIndicator

sealed class WeightedIndicatorState {
    object Loading : WeightedIndicatorState()
    data class Success(val list: List<WeightedIndicator>) : WeightedIndicatorState()
    data class Error(val message: String) : WeightedIndicatorState()
}
