package nl.guidoroos.ticketwave.ui.screens.tickets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.views.LoadingSpinner
import nl.guidoroos.ticketwave.ui.views.TicketPurchaseView
import org.koin.androidx.compose.koinViewModel

@Composable
fun TicketsScreen(
    modifier: Modifier = Modifier,
    onEvent: (AppEvent) -> Unit = {},
    onShowBarcode: (String) -> Unit = {},
    viewModel: TicketsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TicketsScreenView(
        modifier = modifier,
        uiState = uiState,
        onShowBarcode = onShowBarcode
    )
}

@Composable
fun TicketsScreenView(
    uiState: TicketsState,
    onShowBarcode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingSpinner()
        }
    } else if (uiState.ticketGroups.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(Dimens.lg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No purchased tickets yet",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(Dimens.lg),
            verticalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            items(uiState.ticketGroups, key = { it.ticketId }) { group ->
                TicketPurchaseView(
                    group = group,
                    onShowBarcode = onShowBarcode
                )
            }
        }
    }
}
