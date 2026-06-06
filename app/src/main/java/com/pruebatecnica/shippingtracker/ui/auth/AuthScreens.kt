package com.pruebatecnica.shippingtracker.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pruebatecnica.shippingtracker.R
import com.pruebatecnica.shippingtracker.ui.components.BlueActionButton
import com.pruebatecnica.shippingtracker.ui.components.BrandTextField
import com.pruebatecnica.shippingtracker.ui.components.PurpleActionButton
import com.pruebatecnica.shippingtracker.ui.theme.Cyan
import com.pruebatecnica.shippingtracker.ui.theme.PrimaryBlue
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme
import com.pruebatecnica.shippingtracker.ui.theme.ShippingWhite

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(ShippingWhite),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bird_logo),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
        )
    }
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var keepSession by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ShippingWhite)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bird_logo),
            contentDescription = null,
            modifier = Modifier.size(140.dp),
        )
        Spacer(Modifier.height(32.dp))
        BrandTextField(
            value = state.email,
            placeholder = "Usuario",
            onValueChange = onEmailChange,
            leadingIcon = Icons.Filled.Person,
        )
        Spacer(Modifier.height(12.dp))
        BrandTextField(
            value = state.password,
            placeholder = "Contraseña",
            onValueChange = onPasswordChange,
            leadingIcon = Icons.Filled.Lock,
            isPassword = true,
            passwordVisible = passwordVisible,
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = keepSession,
                onCheckedChange = { keepSession = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Cyan,
                    uncheckedColor = Cyan,
                    checkmarkColor = ShippingWhite,
                ),
            )
            Text(text = "Mantener sesión activa", color = Cyan)
        }
        Spacer(Modifier.height(12.dp))
        BlueActionButton(
            text = if (state.isLoading) "Iniciando sesión..." else "Iniciar Sesión",
            onClick = onSubmit,
            enabled = !state.isLoading,
        )
        state.errorMessage?.let { message ->
            Spacer(Modifier.height(8.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "¿Has olvidado tu contraseña?",
            color = PrimaryBlue,
            modifier = Modifier.clickable(onClick = onForgotPassword),
        )
    }
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ShippingWhite)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bird_logo),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Ingresa tu usuario y te enviaremos un correo electrónico para restablecer tu contraseña.",
            color = PrimaryBlue,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(24.dp))
        BrandTextField(
            value = state.email,
            placeholder = "Usuario",
            onValueChange = onEmailChange,
            leadingIcon = Icons.Filled.Person,
        )
        Spacer(Modifier.height(20.dp))
        BlueActionButton(
            text = if (state.isLoading) "Enviando..." else "Enviar",
            onClick = onSubmit,
            enabled = !state.isLoading,
        )
        Spacer(Modifier.height(12.dp))
        PurpleActionButton(text = "Cancelar", onClick = onBack)
        state.successMessage?.let { message ->
            Spacer(Modifier.height(8.dp))
            Text(text = message, color = Cyan, style = MaterialTheme.typography.bodySmall)
        }
        state.errorMessage?.let { message ->
            Spacer(Modifier.height(8.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    ShippingTrackerTheme {
        SplashScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    ShippingTrackerTheme {
        LoginScreen(
            state = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onForgotPassword = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    ShippingTrackerTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordUiState(),
            onEmailChange = {},
            onSubmit = {},
            onBack = {},
        )
    }
}
