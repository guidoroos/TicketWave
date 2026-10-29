package nl.guidoroos.ticketwave.ui.screens.eventdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.theme.Corners
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.utils.formatEventDate
import nl.guidoroos.ticketwave.ui.views.EventAsyncImage
import nl.guidoroos.ticketwave.ui.views.LoadingSpinner
import nl.guidoroos.ticketwave.ui.views.TicketCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventDetailScreen(
    eventId: String,
    modifier: Modifier = Modifier,
    onEvent: (AppEvent) -> Unit = {},
    onNavigateToBasket: () -> Unit = {},
    viewModel: EventDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigateToBasket.collectLatest {
            onNavigateToBasket()
        }
    }

    EventDetailScreenView(
        modifier = modifier,
        uiState = uiState,
        onTicketClick = { ticket ->
            viewModel.addTicketToCart(ticket.id, onEvent)
        }
    )
}

@Composable
fun EventDetailScreenView(
    uiState: EventDetailUiState,
    onTicketClick: (nl.guidoroos.ticketwave.ui.model.UITicket) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingSpinner()
        }
    } else {
        val event = uiState.event
        if (event == null) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.errorMessage ?: "Event not found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                EventAsyncImage(
                    imageUrl = event.imageUrl,
                    contentDescription = event.title,
                    height = 250.dp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.lg),
                    verticalArrangement = Arrangement.spacedBy(Dimens.md)
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (!event.description.isNullOrBlank()) {
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = event.venue.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = event.startsAt?.let { formatEventDate(it) } ?: "TBD",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.sm))

                    Text(
                        text = "Available tickets",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        event.tickets.forEachIndexed { index, ticket ->
                            val isFirst = index == 0
                            val isLast = index == event.tickets.size - 1
                            val shape = when {
                                isFirst && isLast -> Corners.medium
                                isFirst -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                                isLast -> RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
                                else -> RoundedCornerShape(0.dp)
                            }
                            val itemModifier = if (index > 0) Modifier.offset(y = (-1).dp) else Modifier
                            TicketCard(
                                ticket = ticket,
                                onClick = { onTicketClick(ticket) },
                                modifier = itemModifier,
                                shape = shape
                            )
                        }
                    }
                }
            }
        }
    }
}
