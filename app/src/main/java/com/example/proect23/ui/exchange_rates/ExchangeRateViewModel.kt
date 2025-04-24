package com.example.proect23.ui.exchange_rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExchangeRateViewModel : ViewModel() {
    private val repo = ExchangeRateRepository()

    private val _state = MutableStateFlow<ExchangeRateState>(ExchangeRateState.Loading)
    val state: StateFlow<ExchangeRateState> = _state

    init { fetchAll() }

    fun fetchAll() {
        viewModelScope.launch {
            _state.value = ExchangeRateState.Loading
            repo.getAll()
                .onSuccess { _state.value = ExchangeRateState.Success(it) }
                .onFailure { _state.value = ExchangeRateState.Error(it.localizedMessage ?: "Error") }
        }
    }
}
