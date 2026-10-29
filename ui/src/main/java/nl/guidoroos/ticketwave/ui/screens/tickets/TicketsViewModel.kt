package nl.guidoroos.ticketwave.ui.screens.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import nl.guidoroos.ticketwave.domain.repository.AuthRepository
import nl.guidoroos.ticketwave.domain.repository.UserRepository
import nl.guidoroos.ticketwave.ui.model.UIOwnedTicketGroup

data class TicketsState(
    val ticketGroups: List<UIOwnedTicketGroup> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class TicketsViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsState(isLoading = false))
    val uiState: StateFlow<TicketsState> = _uiState.asStateFlow()

    init {
        loadTickets()
    }

    fun loadTickets() {
        viewModelScope.launch(ioDispatcher) {
            val token = authRepository.getToken()
            if (token.isNullOrBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Not authenticated") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = userRepository.getTicketWallet(token)
            result.fold(
                onSuccess = { domainTickets ->
                    val grouped = domainTickets.groupBy { Pair(it.eventId ?: it.label, it.label) }
                    val ticketGroups = grouped.map { (_, tickets) ->
                        val first = tickets.first()
                        val parsedStartsAt = first.startsAt?.let {
                            try { LocalDateTime.parse(it.removeSuffix("Z")) } catch (_: Exception) { null }
                        }
                        UIOwnedTicketGroup(
                            ticketId = first.id,
                            eventTitle = first.eventTitle ?: "Concert Ticket",
                            ticketLabel = first.label,
                            priceCents = first.priceCents,
                            currency = first.currency,
                            count = tickets.size,
                            eventImageUrl = first.eventImageUrl ?: "",
                            venueName = first.venueName,
                            startsAt = parsedStartsAt,
                            barcodes = tickets.map { it.barcode }
                        )
                    }
                    _uiState.update { it.copy(ticketGroups = ticketGroups, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Failed to load tickets") }
                }
            )
        }
    }
}
