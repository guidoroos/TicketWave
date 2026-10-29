package nl.guidoroos.ticketwave.ui.screens.basket

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
import nl.guidoroos.ticketwave.ui.mappers.toUiModel
import nl.guidoroos.ticketwave.ui.model.UITicket
import nl.guidoroos.ticketwave.ui.model.UITicketWithEvent
import nl.guidoroos.ticketwave.ui.utils.isValidCreditCard

data class BasketItemState(
    val item: UITicketWithEvent,
    val count: Int = 1
)

data class BasketState(
    val items: List<BasketItemState> = emptyList(),
    val creditCardNumber: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val totalPriceCents: Int
        get() = items.sumOf { it.item.ticket.priceCents * it.count }
}

class BasketViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(BasketState(isLoading = false))
    val uiState: StateFlow<BasketState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch(ioDispatcher) {
            val token = authRepository.getToken()
            if (token.isNullOrBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Not authenticated") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = userRepository.getCart(token)
            result.fold(
                onSuccess = { domainCart ->
                    val uiCart = domainCart.toUiModel()
                    val grouped = uiCart.items.groupBy { it.ticketId }
                    val items = grouped.map { (_, lines) ->
                        val first = lines.first()
                        BasketItemState(
                            item = UITicketWithEvent(
                                eventId = first.eventId,
                                eventTitle = first.eventTitle,
                                ticket = UITicket(
                                    id = first.ticketId,
                                    label = first.ticketLabel,
                                    priceCents = first.priceCents,
                                    currency = first.currency,
                                    quantityAvailable = 99
                                ),
                                eventImageUrl = first.eventImageUrl,
                                venueName = first.venueName,
                                startsAt = first.startsAt
                            ),
                            count = lines.size
                        )
                    }
                    _uiState.update {
                        it.copy(
                            items = items,
                            creditCardNumber = uiCart.creditCardNumber,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load cart"
                        )
                    }
                }
            )
        }
    }

    fun increment(ticketId: String, onEvent: (AppEvent) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val token = authRepository.getToken()
            if (token.isNullOrBlank()) {
                onEvent(AppEvent.ShowDialog("Authentication Error", "Please log in again."))
                return@launch
            }
            val result = userRepository.addTicketToCart(token, ticketId)
            result.fold(
                onSuccess = { loadCart() },
                onFailure = { error -> onEvent(AppEvent.ShowDialog("Error", error.message ?: "Failed to add ticket")) }
            )
        }
    }

    fun decrement(ticketId: String, onEvent: (AppEvent) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val token = authRepository.getToken()
            if (token.isNullOrBlank()) {
                onEvent(AppEvent.ShowDialog("Authentication Error", "Please log in again."))
                return@launch
            }
            val result = userRepository.removeTicketFromCart(token, ticketId)
            result.fold(
                onSuccess = { loadCart() },
                onFailure = { error -> onEvent(AppEvent.ShowDialog("Error", error.message ?: "Failed to remove ticket")) }
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
                    loadCart()
                    onSuccess()
                },
                onFailure = { error ->
                    onEvent(AppEvent.ShowDialog("Error", error.message ?: "Failed to save credit card"))
                }
            )
        }
    }
}
