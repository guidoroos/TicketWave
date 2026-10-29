package nl.guidoroos.ticketwave.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.theme.TicketWaveTheme
import nl.guidoroos.ticketwave.ui.views.LoadingSpinner
import nl.guidoroos.ticketwave.ui.views.PrimaryButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onEvent: (AppEvent) -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreenView(
        modifier = modifier,
        uiState = uiState,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = {
            viewModel.login(onEvent, onLoginSuccess)
        }
    )
}

@Composable
fun LoginScreenView(
    uiState: LoginUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.lg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login to TicketWave",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = Dimens.md)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.md),
                verticalArrangement = Arrangement.spacedBy(Dimens.md)
            ) {
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = onUsernameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Username", style = MaterialTheme.typography.bodyMedium) },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    isError = uiState.usernameError != null,
                    supportingText = {
                        if (uiState.usernameError != null) {
                            Text(
                                text = uiState.usernameError,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Password", style = MaterialTheme.typography.bodyMedium) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    isError = uiState.passwordError != null,
                    supportingText = {
                        if (uiState.passwordError != null) {
                            Text(
                                text = uiState.passwordError,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }

            PrimaryButton(
                text = "Login",
                onClick = onLoginClick,
                enabled = !uiState.isLoading,
                modifier = Modifier.padding(top = Dimens.sm)
            )
        }

        if (uiState.isLoading) {
            LoadingSpinner()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenViewPreview() {
    TicketWaveTheme {
        LoginScreenView(
            uiState = LoginUiState(username = "testuser", password = "password123"),
            onUsernameChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}
