package nl.guidoroos.ticketwave.ui.screens.eventdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.guidoroos.ticketwave.domain.repository.AuthRepository
import nl.guidoroos.ticketwave.domain.repository.EventsRepository
import nl.guidoroos.ticketwave.domain.repository.UserRepository
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.mappers.toUiModel
import nl.guidoroos.ticketwave.ui.model.UIEvent

data class EventDetailUiState(
    val event: UIEvent? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class EventDetailViewModel(
    private val eventsRepository: EventsRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: String = savedStateHandle["eventId"] ?: ""

    private val _uiState = MutableStateFlow(EventDetailUiState(isLoading = false))
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    private val _navigateToBasket = MutableSharedFlow<Unit>()
    val navigateToBasket: SharedFlow<Unit> = _navigateToBasket.asSharedFlow()

    init {
        loadEvent()
    }

    private fun loadEvent() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = eventsRepository.getEvent(eventId)
            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = { domainEvent ->
                    _uiState.update { it.copy(event = domainEvent?.toUiModel()) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(errorMessage = error.message ?: "Failed to load event") }
                }
            )
        }
    }

    fun addTicketToCart(ticketId: String, onEvent: (AppEvent) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val token = authRepository.getToken()
            if (token.isNullOrBlank()) {
                onEvent(AppEvent.ShowDialog("Authentication Error", "Please log in again."))
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            val result = userRepository.addTicketToCart(token, ticketId)
            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    _navigateToBasket.emit(Unit)
                },
                onFailure = { error ->
                    val msg = error.message ?: "Failed to add ticket"
                    onEvent(AppEvent.ShowDialog("Error", msg))
                }
            )
        }
    }
}
