package com.pruebatecnica.shippingtracker.ui.auth

import com.pruebatecnica.shippingtracker.domain.AppResult
import com.pruebatecnica.shippingtracker.domain.AuthRepository
import com.pruebatecnica.shippingtracker.domain.AuthToken
import com.pruebatecnica.shippingtracker.domain.ForgotPasswordResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
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
class LoginViewModelTest {
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
    fun updateEmailAndPassword_reflectsInputState() {
        val viewModel = LoginViewModel(FakeLoginAuthRepository())

        viewModel.updateEmail("user@example.com")
        viewModel.updatePassword("secret")

        assertEquals("user@example.com", viewModel.uiState.value.email)
        assertEquals("secret", viewModel.uiState.value.password)
    }

    @Test
    fun submitLogin_successStoresSessionTokenAndClearsError() = runTest {
        val viewModel = LoginViewModel(
            FakeLoginAuthRepository(loginResult = AppResult.Success(AuthToken("token", "2026-12-31"))),
        )
        viewModel.updateEmail("user@example.com")
        viewModel.updatePassword("secret")

        viewModel.submitLogin()
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(AuthToken("token", "2026-12-31"), state.sessionToken)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun submitLogin_failureShowsApiMessageAndKeepsSessionEmpty() = runTest {
        val viewModel = LoginViewModel(
            FakeLoginAuthRepository(loginResult = AppResult.Error("Invalid credentials")),
        )
        viewModel.updateEmail("bad@example.com")
        viewModel.updatePassword("wrong")

        viewModel.submitLogin()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.sessionToken)
        assertEquals("Invalid credentials", state.errorMessage)
    }
}

private class FakeLoginAuthRepository(
    private val loginResult: AppResult<AuthToken> = AppResult.Error("not configured"),
) : AuthRepository {
    override suspend fun login(email: String, password: String): AppResult<AuthToken> = loginResult

    override suspend fun forgotPassword(email: String): AppResult<ForgotPasswordResult> =
        AppResult.Error("not configured")
}
