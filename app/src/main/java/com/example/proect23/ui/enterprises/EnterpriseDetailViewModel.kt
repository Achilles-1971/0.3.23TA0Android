package com.example.proect23.ui.enterprises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.model.Enterprise
import com.example.proect23.data.repository.EnterpriseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EnterpriseDetailViewModel : ViewModel() {

    private val repo = EnterpriseRepository()

    private val _state = MutableStateFlow<EnterpriseDetailState>(EnterpriseDetailState.Loading)
    val state: StateFlow<EnterpriseDetailState> = _state

    /** Загружает данные одного предприятия */
    fun fetchById(id: Int) {
        viewModelScope.launch {
            _state.value = EnterpriseDetailState.Loading
            repo.getById(id)
                .onSuccess { ent ->
                    _state.value = EnterpriseDetailState.Success(ent)
                }
                .onFailure { err ->
                    _state.value = EnterpriseDetailState.Error(
                        err.localizedMessage ?: "Не удалось загрузить данные"
                    )
                }
        }
    }

    /** Обновляет предприятие на сервере */
    fun update(enterprise: Enterprise) {
        viewModelScope.launch {
            _state.value = EnterpriseDetailState.Loading
            println("Starting update for enterprise: $enterprise")
            repo.update(enterprise)
                .onSuccess { updated ->
                    println("Update successful: $updated")
                    _state.value = EnterpriseDetailState.Success(updated)
                }
                .onFailure { err ->
                    println("Update failed: ${err.message}")
                    err.printStackTrace()
                    _state.value = EnterpriseDetailState.Error(
                        err.localizedMessage ?: "Не удалось сохранить изменения"
                    )
                }
        }
    }

    /** Удаляет предприятие на сервере */
    fun delete(id: Int) {
        viewModelScope.launch {
            repo.delete(id)
                .onSuccess {
                    _state.value = EnterpriseDetailState.Deleted
                }
                .onFailure { err ->
                    _state.value = EnterpriseDetailState.Error(
                        err.localizedMessage ?: "Не удалось удалить"
                    )
                }
        }
    }
}