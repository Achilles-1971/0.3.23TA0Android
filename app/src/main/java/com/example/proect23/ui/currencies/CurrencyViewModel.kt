package com.example.proect23.ui.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {
    private val repo = CurrencyRepository()

    private val _state = MutableStateFlow<CurrencyState>(CurrencyState.Loading)
    val state: StateFlow<CurrencyState> = _state

    init { fetchAll() }

    fun fetchAll() {
        viewModelScope.launch {
            _state.value = CurrencyState.Loading
            repo.getAll()
                .onSuccess { _state.value = CurrencyState.Success(it) }
                .onFailure { _state.value = CurrencyState.Error(it.localizedMessage ?: "Error") }
        }
    }
}
