package com.example.proect23.ui.indicator_values

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.repository.IndicatorValueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IndicatorValueViewModel : ViewModel() {
    private val repo = IndicatorValueRepository()

    private val _state = MutableStateFlow<IndicatorValueState>(IndicatorValueState.Loading)
    val state: StateFlow<IndicatorValueState> = _state

    init {
        fetchAll()
    }

    fun fetchAll(
        enterpriseId: Int? = null,
        indicatorId: Int? = null,
        fromDate: String? = null,
        toDate: String? = null,
        targetCurrency: String? = null
    ) {
        viewModelScope.launch {
            _state.value = IndicatorValueState.Loading
            repo.getAll(enterpriseId, indicatorId, fromDate, toDate, targetCurrency)
                .onSuccess { _state.value = IndicatorValueState.Success(it) }
                .onFailure { _state.value = IndicatorValueState.Error(it.localizedMessage ?: "Error") }
        }
    }
}
