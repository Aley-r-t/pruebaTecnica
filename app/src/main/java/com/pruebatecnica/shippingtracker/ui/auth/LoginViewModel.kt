package com.pruebatecnica.shippingtracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruebatecnica.shippingtracker.domain.AppResult
import com.pruebatecnica.shippingtracker.domain.AuthRepository
import com.pruebatecnica.shippingtracker.domain.AuthToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val sessionToken: AuthToken? = null,
    val errorMessage: String? = null,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = mutableUiState

    fun updateEmail(email: String) {
        mutableUiState.update { state -> state.copy(email = email) }
    }

    fun updatePassword(password: String) {
        mutableUiState.update { state -> state.copy(password = password) }
    }

    fun submitLogin() {
        val current = mutableUiState.value
        mutableUiState.update { state -> state.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            when (val result = authRepository.login(current.email, current.password)) {
                is AppResult.Success -> mutableUiState.update { state ->
                    state.copy(isLoading = false, sessionToken = result.value, errorMessage = null)
                }
                is AppResult.Error -> mutableUiState.update { state ->
                    state.copy(isLoading = false, sessionToken = null, errorMessage = result.message)
                }
            }
        }
    }
}
