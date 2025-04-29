package com.example.proect23.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.model.TokenResponse
import com.example.proect23.data.repository.UserRepository
import com.example.proect23.util.PrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val token: TokenResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repo = UserRepository()

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> get() = _state

    fun register(username: String, password: String) {
        if (password.length < 8) {
            _state.value = AuthState.Error("Пароль должен содержать минимум 8 символов")
            return
        }
        performAuth { repo.register(username, password) }
    }

    fun login(username: String, password: String) {
        if (password.length < 8) {
            _state.value = AuthState.Error("Пароль должен содержать минимум 8 символов")
            return
        }
        performAuth { repo.login(username, password) }
    }

    private fun performAuth(block: suspend () -> Result<TokenResponse>) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = block()
            result
                .onSuccess { token ->
                    PrefsManager.token = token.accessToken
                    PrefsManager.refreshToken = token.refreshToken
                    _state.value = AuthState.Success(token)
                }
                .onFailure { error ->
                    _state.value = AuthState.Error(error.message ?: "Неизвестная ошибка")
                }
        }
    }
}