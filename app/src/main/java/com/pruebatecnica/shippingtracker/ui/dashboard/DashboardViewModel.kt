package com.pruebatecnica.shippingtracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruebatecnica.shippingtracker.domain.AppResult
import com.pruebatecnica.shippingtracker.domain.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val markers: List<MapVehicleMarker> = emptyList(),
    val errorMessage: String? = null,
)

class DashboardViewModel(
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = mutableUiState

    fun loadVehicles() {
        mutableUiState.update { state -> state.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            when (val result = vehicleRepository.getVehicles()) {
                is AppResult.Success -> mutableUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        markers = result.value.toMapMarkers(),
                        errorMessage = null,
                    )
                }
                is AppResult.Error -> mutableUiState.update { state ->
                    state.copy(isLoading = false, markers = emptyList(), errorMessage = result.message)
                }
            }
        }
    }
}
