package com.pruebatecnica.shippingtracker.ui.dashboard

import com.pruebatecnica.shippingtracker.domain.AppResult
import com.pruebatecnica.shippingtracker.domain.Vehicle
import com.pruebatecnica.shippingtracker.domain.VehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadVehicles_successMapsVehiclesToMarkers() = runTest {
        val viewModel = DashboardViewModel(
            FakeVehicleRepository(
                AppResult.Success(
                    listOf(
                        Vehicle(1, "AAA-111", 30.0, -12.0, -77.0, 90.0, "moving"),
                        Vehicle(2, "BBB-222", 0.0, -12.5, -77.5, 0.0, "stopped"),
                    ),
                ),
            ),
        )

        viewModel.loadVehicles()
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.errorMessage)
        assertEquals(2, state.markers.size)
        assertEquals("AAA-111", state.markers[0].plate)
        assertEquals("0.0 km/h", state.markers[1].speedLabel)
    }

    @Test
    fun loadVehicles_failureShowsErrorAndClearsMarkers() = runTest {
        val viewModel = DashboardViewModel(FakeVehicleRepository(AppResult.Error("Vehicles unavailable")))

        viewModel.loadVehicles()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Vehicles unavailable", state.errorMessage)
        assertEquals(emptyList<MapVehicleMarker>(), state.markers)
    }
}

private class FakeVehicleRepository(
    private val result: AppResult<List<Vehicle>>,
) : VehicleRepository {
    override suspend fun getVehicles(): AppResult<List<Vehicle>> = result
}
