package com.example.proect23.ui.indicators

import com.example.proect23.data.model.Indicator

sealed class IndicatorState {
    object Loading : IndicatorState()
    data class Success(val list: List<Indicator>) : IndicatorState()
    data class Error(val message: String) : IndicatorState()
}
