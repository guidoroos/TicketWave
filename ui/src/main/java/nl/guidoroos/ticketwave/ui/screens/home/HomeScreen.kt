package nl.guidoroos.ticketwave.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDateTime
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.model.UIEvent
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.theme.TicketWaveTheme
import nl.guidoroos.ticketwave.ui.views.EventCard
import nl.guidoroos.ticketwave.ui.views.LoadingSpinner
import nl.guidoroos.ticketwave.ui.views.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onEvent: (AppEvent) -> Unit = {},
    onEventClick: (UIEvent) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(initialValue = HomeUiState(isLoading = false))
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    HomeScreenView(
        modifier = modifier,
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onEventClick = onEventClick
    )
}

@Composable
fun HomeScreenView(
    uiState: HomeUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onEventClick: (UIEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = Dimens.sm),
        verticalArrangement = Arrangement.spacedBy(Dimens.md)
    ) {
        SearchBar(
            textState = searchQuery,
            onTextChange = onSearchQueryChange
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingSpinner()
            }
        } else if (uiState.events.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No events found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.lg),
                verticalArrangement = Arrangement.spacedBy(Dimens.md)
            ) {
                items(uiState.events, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        onClick = { onEventClick(event) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenViewPreview() {
    TicketWaveTheme {
        HomeScreenView(
            uiState = HomeUiState(
                events = listOf(
                    UIEvent(
                        id = "1",
                        title = "Summer Music Festival",
                        description = "Amazing open air concert",
                        startsAt = LocalDateTime(2026, 9, 15, 20, 0),
                        venue = nl.guidoroos.ticketwave.ui.model.UIVenue("v1", "Ziggo Dome", "url"),
                        tickets = listOf(
                            nl.guidoroos.ticketwave.ui.model.UITicket("t1", "Regular", 2500, "EUR", 100)
                        ),
                        imageUrl = "url"
                    )
                )
            ),
            searchQuery = "",
            onSearchQueryChange = {}
        )
    }
}
