package nl.guidoroos.ticketwave.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import nl.guidoroos.ticketwave.domain.repository.EventsRepository
import nl.guidoroos.ticketwave.ui.mappers.toUiModel
import nl.guidoroos.ticketwave.ui.model.UIEvent
import kotlin.time.Duration.Companion.milliseconds

data class HomeUiState(
    val events: List<UIEvent> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")
    val searchQuery: StateFlow<String> = searchQueryFlow.asStateFlow()

    fun onSearchQueryChange(query: String) {
        searchQueryFlow.value = query
    }

    val uiState: Flow<HomeUiState> = searchQueryFlow
        .debounce(if (searchQuery.value.isEmpty()) 0.milliseconds else 500.milliseconds)
        .flatMapLatest { query ->
            flow {
                emit(HomeUiState(isLoading = true))
                val result = if (query.isBlank()) {
                    eventsRepository.events()
                } else {
                    eventsRepository.searchEvents(query)
                }
                result.fold(
                    onSuccess = { domainEvents ->
                        emit(
                            HomeUiState(
                                events = domainEvents.map { it.toUiModel() },
                                isLoading = false
                            )
                        )
                    },
                    onFailure = { error ->
                        emit(
                            HomeUiState(
                                isLoading = false,
                                errorMessage = error.message ?: "Failed to load events"
                            )
                        )
                    }
                )
            }.flowOn(Dispatchers.IO)
        }
}
