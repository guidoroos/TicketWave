package nl.guidoroos.ticketwave.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.guidoroos.ticketwave.domain.repository.AuthRepository
import nl.guidoroos.ticketwave.ui.events.AppEvent

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(newVal: String) {
        _uiState.update { it.copy(username = newVal, usernameError = null, generalError = null) }
    }

    fun onPasswordChange(newVal: String) {
        _uiState.update { it.copy(password = newVal, passwordError = null, generalError = null) }
    }

    fun login(onEvent: (AppEvent) -> Unit, onSuccess: () -> Unit) {
        val currentState = _uiState.value
        val isUsernameEmpty = currentState.username.isBlank()
        val isPasswordEmpty = currentState.password.isBlank()

        if (isUsernameEmpty || isPasswordEmpty) {
            _uiState.update {
                it.copy(
                    usernameError = if (isUsernameEmpty) "Username cannot be empty" else null,
                    passwordError = if (isPasswordEmpty) "Password cannot be empty" else null
                )
            }
            onEvent(AppEvent.ShowSnackbar("Please fill in all required fields"))
            return
        }

        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, usernameError = null, passwordError = null, generalError = null) }
            val result = authRepository.login(currentState.username, currentState.password)
            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    onEvent(AppEvent.ShowSnackbar("Login successful!"))
                    onSuccess()
                },
                onFailure = { error ->
                    val errorMsg = error.message ?: "Login failed"
                    _uiState.update { it.copy(generalError = errorMsg) }
                    onEvent(AppEvent.ShowDialog("Login Error", errorMsg))
                }
            )
        }
    }
}
