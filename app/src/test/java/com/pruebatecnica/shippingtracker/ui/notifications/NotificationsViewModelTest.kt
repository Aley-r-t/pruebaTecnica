package com.pruebatecnica.shippingtracker.ui.notifications

import com.pruebatecnica.shippingtracker.domain.AppResult
import com.pruebatecnica.shippingtracker.domain.NotificationItem
import com.pruebatecnica.shippingtracker.domain.NotificationRepository
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
class NotificationsViewModelTest {
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
    fun loadNotifications_successShowsInvoiceStatusAndCreatedDate() = runTest {
        val viewModel = NotificationsViewModel(
            FakeNotificationRepository(
                AppResult.Success(
                    listOf(
                        NotificationItem(1, "INV-001", "pending", "2026-06-05"),
                        NotificationItem(2, "INV-002", "paid", "2026-06-06"),
                    ),
                ),
            ),
        )

        viewModel.loadNotifications()
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.errorMessage)
        assertEquals(2, state.notifications.size)
        assertEquals("INV-001", state.notifications[0].invoiceNumber)
        assertEquals("paid", state.notifications[1].status)
    }

    @Test
    fun loadNotifications_failureShowsError() = runTest {
        val viewModel = NotificationsViewModel(FakeNotificationRepository(AppResult.Error("Notifications unavailable")))

        viewModel.loadNotifications()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Notifications unavailable", state.errorMessage)
        assertEquals(emptyList<NotificationItem>(), state.notifications)
    }

    @Test
    fun deleteNotification_removesCardLocallyWithoutRepositoryCall() = runTest {
        val repository = FakeNotificationRepository(
            AppResult.Success(
                listOf(
                    NotificationItem(1, "INV-001", "pending", "2026-06-05"),
                    NotificationItem(2, "INV-002", "paid", "2026-06-06"),
                ),
            ),
        )
        val viewModel = NotificationsViewModel(repository)
        viewModel.loadNotifications()
        advanceUntilIdle()

        viewModel.deleteNotification(1)

        assertEquals(1, viewModel.uiState.value.notifications.size)
        assertEquals("INV-002", viewModel.uiState.value.notifications.single().invoiceNumber)
        assertEquals(1, repository.loadCalls)
    }
}

private class FakeNotificationRepository(
    private val result: AppResult<List<NotificationItem>>,
) : NotificationRepository {
    var loadCalls: Int = 0

    override suspend fun getNotifications(): AppResult<List<NotificationItem>> {
        loadCalls += 1
        return result
    }
}
