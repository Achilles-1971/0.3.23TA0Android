package com.example.proect23.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proect23.data.model.UserProfileResponse
import com.example.proect23.data.repository.ProfileRepository
import com.example.proect23.util.UnauthorizedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.io.File

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: UserProfileResponse) : ProfileState()
    data class Error(val message: String) : ProfileState()
    object Unauthorized : ProfileState()
}

class ProfileViewModel : ViewModel() {
    private val repo = ProfileRepository()

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            Log.d("ProfileViewModel", "Loading profile")
            repo.loadProfile()
                .onSuccess { profile ->
                    Log.d("ProfileViewModel", "Profile loaded: ${profile.username}, avatarUrl: ${profile.avatarUrl}")
                    _state.value = ProfileState.Success(profile)
                }
                .onFailure { error ->
                    Log.e("ProfileViewModel", "Profile load failed: ${error.message}")
                    when (error) {
                        is UnauthorizedException -> _state.value = ProfileState.Unauthorized
                        else -> _state.value = ProfileState.Error(error.message ?: "Ошибка загрузки профиля")
                    }
                }
        }
    }

    fun uploadAvatar(file: File) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            Log.d("ProfileViewModel", "Uploading avatar: ${file.absolutePath}")
            repo.uploadAvatar(file)
                .onSuccess { profile ->
                    Log.d("ProfileViewModel", "Avatar uploaded successfully: ${profile.avatarUrl}")
                    _state.value = ProfileState.Success(profile)
                }
                .onFailure { error ->
                    Log.e("ProfileViewModel", "Avatar upload failed: ${error.message}")
                    when {
                        error is UnauthorizedException -> _state.value = ProfileState.Unauthorized
                        error.message?.contains("Only JPEG or PNG") == true ->
                            _state.value = ProfileState.Error("Только изображения JPEG или PNG")
                        error.message?.contains("File size exceeds") == true ->
                            _state.value = ProfileState.Error("Размер файла превышает 5 МБ")
                        else -> _state.value = ProfileState.Error(error.message ?: "Ошибка загрузки аватарки")
                    }
                }
        }
    }
}