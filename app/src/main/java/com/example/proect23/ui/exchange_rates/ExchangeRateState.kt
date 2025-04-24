package com.example.proect23.ui.exchange_rates

import com.example.proect23.data.model.ExchangeRate

sealed class ExchangeRateState {
    object Loading : ExchangeRateState()
    data class Success(val list: List<ExchangeRate>) : ExchangeRateState()
    data class Error(val message: String) : ExchangeRateState()
}
