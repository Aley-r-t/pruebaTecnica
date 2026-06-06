package com.pruebatecnica.shippingtracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruebatecnica.shippingtracker.domain.AppResult
import com.pruebatecnica.shippingtracker.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = mutableUiState

    fun updateEmail(email: String) {
        mutableUiState.update { state -> state.copy(email = email) }
    }

    fun submit() {
        val current = mutableUiState.value
        mutableUiState.update { state ->
            state.copy(isLoading = true, successMessage = null, errorMessage = null)
        }
        viewModelScope.launch {
            when (val result = authRepository.forgotPassword(current.email)) {
                is AppResult.Success -> mutableUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        successMessage = result.value.message,
                        errorMessage = null,
                    )
                }
                is AppResult.Error -> mutableUiState.update { state ->
                    state.copy(isLoading = false, successMessage = null, errorMessage = result.message)
                }
            }
        }
    }
}
