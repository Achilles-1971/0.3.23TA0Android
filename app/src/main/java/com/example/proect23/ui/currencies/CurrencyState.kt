package com.example.proect23.ui.currencies

import com.example.proect23.data.model.Currency

sealed class CurrencyState {
    object Loading : CurrencyState()
    data class Success(val list: List<Currency>) : CurrencyState()
    data class Error(val message: String) : CurrencyState()
}
