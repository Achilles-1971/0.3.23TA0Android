package com.example.proect23.ui.weighted_indicators

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.repository.WeightedIndicatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeightedIndicatorViewModel : ViewModel() {
    private val repo = WeightedIndicatorRepository()

    private val _state = MutableStateFlow<WeightedIndicatorState>(WeightedIndicatorState.Loading)
    val state: StateFlow<WeightedIndicatorState> = _state

    /** По умолчанию возьмём enterpriseId = 1 (или передавайте из другого фрагмента) **/
    init { fetchAll(enterpriseId = 1) }

    fun fetchAll(
        enterpriseId: Int,
        indicatorId: Int? = null,
        fromDate: String? = null,
        toDate: String? = null,
        targetCurrency: String? = null
    ) {
        viewModelScope.launch {
            _state.value = WeightedIndicatorState.Loading
            repo.getAll(enterpriseId, indicatorId, fromDate, toDate, targetCurrency)
                .onSuccess { _state.value = WeightedIndicatorState.Success(it) }
                .onFailure { _state.value = WeightedIndicatorState.Error(it.localizedMessage ?: "Error") }
        }
    }
}
