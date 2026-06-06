package com.pruebatecnica.shippingtracker.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruebatecnica.shippingtracker.domain.AppResult
import com.pruebatecnica.shippingtracker.domain.NotificationItem
import com.pruebatecnica.shippingtracker.domain.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationItem> = emptyList(),
    val errorMessage: String? = null,
)

class NotificationsViewModel(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = mutableUiState

    fun loadNotifications() {
        mutableUiState.update { state -> state.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            when (val result = notificationRepository.getNotifications()) {
                is AppResult.Success -> mutableUiState.update { state ->
                    state.copy(isLoading = false, notifications = result.value, errorMessage = null)
                }
                is AppResult.Error -> mutableUiState.update { state ->
                    state.copy(isLoading = false, notifications = emptyList(), errorMessage = result.message)
                }
            }
        }
    }

    fun deleteNotification(id: Int) {
        mutableUiState.update { state ->
            state.copy(notifications = state.notifications.filterNot { notification -> notification.id == id })
        }
    }
}
