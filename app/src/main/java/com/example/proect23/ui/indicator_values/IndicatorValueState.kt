package com.example.proect23.ui.indicator_values

import com.example.proect23.data.model.IndicatorValue

sealed class IndicatorValueState {
    object Loading : IndicatorValueState()
    data class Success(val list: List<IndicatorValue>) : IndicatorValueState()
    data class Error(val message: String) : IndicatorValueState()
}
