package com.example.proect23.ui.indicators

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.repository.IndicatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IndicatorViewModel : ViewModel() {
    private val repo = IndicatorRepository()

    private val _state = MutableStateFlow<IndicatorState>(IndicatorState.Loading)
    val state: StateFlow<IndicatorState> = _state

    init { fetchAll() }

    fun fetchAll() {
        viewModelScope.launch {
            _state.value = IndicatorState.Loading
            repo.getAll()
                .onSuccess { _state.value = IndicatorState.Success(it) }
                .onFailure { _state.value = IndicatorState.Error(it.localizedMessage ?: "Error") }
        }
    }
}
