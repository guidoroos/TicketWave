package nl.guidoroos.ticketwave.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.guidoroos.ticketwave.domain.repository.AuthRepository
import nl.guidoroos.ticketwave.domain.repository.UserRepository
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.utils.isValidCreditCard

data class SettingState(
    val firstName: String = "",
    val lastName: String = "",
    val age: Int = 0,
    val creditCardNumber: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
}

class SettingViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingState(isLoading = false))
    val uiState: StateFlow<SettingState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch(ioDispatcher) {
            val token = authRepository.getToken()
            if (token.isNullOrBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Not authenticated") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = userRepository.getUserProfile(token)
            result.fold(
                onSuccess = { profile ->
                    _uiState.update {
                        it.copy(
                            firstName = profile.firstName,
                            lastName = profile.lastName,
                            age = profile.age,
                            creditCardNumber = profile.creditCardNumber,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load profile"
                        )
                    }
                }
            )
        }
    }

    fun saveCreditCard(cardNumber: String, onEvent: (AppEvent) -> Unit, onSuccess: () -> Unit) {
        if (!isValidCreditCard(cardNumber)) {
            onEvent(AppEvent.ShowDialog("Invalid Card", "Please enter a valid credit card number."))
            return
        }
        viewModelScope.launch(ioDispatcher) {
            val token = authRepository.getToken()
            if (token.isNullOrBlank()) {
                onEvent(AppEvent.ShowDialog("Authentication Error", "Please log in again."))
                return@launch
            }
            val result = userRepository.saveCreditCard(token, cardNumber)
            result.fold(
                onSuccess = {
                    onEvent(AppEvent.ShowSnackbar("Credit card updated successfully"))
                    loadProfile()
                    onSuccess()
                },
                onFailure = { error ->
                    onEvent(AppEvent.ShowDialog("Error", error.message ?: "Failed to save credit card"))
                }
            )
        }
    }

    fun logout(onEvent: (AppEvent) -> Unit, onSuccess: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val result = authRepository.logout()
            result.fold(
                onSuccess = {
                    onEvent(AppEvent.ShowSnackbar("Logged out successfully"))
                    onSuccess()
                },
                onFailure = { error ->
                    onEvent(AppEvent.ShowDialog("Logout Error", error.message ?: "Logout failed"))
                }
            )
        }
    }
}
