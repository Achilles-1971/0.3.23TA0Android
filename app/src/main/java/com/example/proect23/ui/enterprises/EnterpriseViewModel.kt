package com.example.proect23.ui.enterprises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.repository.EnterpriseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EnterpriseViewModel : ViewModel() {
    private val repo = EnterpriseRepository()

    private val _state = MutableStateFlow<EnterpriseState>(EnterpriseState.Loading)
    val state: StateFlow<EnterpriseState> get() = _state

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            _state.value = EnterpriseState.Loading
            repo.getAll()
                .onSuccess { list -> _state.value = EnterpriseState.Success(list) }
                .onFailure { err -> _state.value = EnterpriseState.Error(err.localizedMessage ?: "Error") }
        }
    }
}
