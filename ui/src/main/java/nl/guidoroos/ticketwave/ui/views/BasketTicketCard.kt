package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import nl.guidoroos.ticketwave.ui.model.UITicket
import nl.guidoroos.ticketwave.ui.model.UITicketWithEvent
import nl.guidoroos.ticketwave.ui.theme.Corners
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.theme.TicketWaveTheme
import nl.guidoroos.ticketwave.ui.theme.WarningOrange
import nl.guidoroos.ticketwave.ui.utils.formatCurrency
import nl.guidoroos.ticketwave.ui.utils.formatEventDate

@Composable
fun BasketTicketCard(
    item: UITicketWithEvent,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ticket = item.ticket
    val formattedPrice = formatCurrency(ticket.priceCents, ticket.currency)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Corners.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image with date chip overlay (like EventCard)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                EventAsyncImage(
                    imageUrl = item.eventImageUrl,
                    contentDescription = item.eventTitle,
                    height = 160.dp
                )

                item.startsAt?.let {
                    Surface(
                        modifier = Modifier
                            .padding(Dimens.md)
                            .align(Alignment.TopStart),
                        shape = Corners.small,
                        color = WarningOrange,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Text(
                            text = formatEventDate(it),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(
                                horizontal = Dimens.md,
                                vertical = Dimens.sm
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.lg),
                verticalArrangement = Arrangement.spacedBy(Dimens.md)
            ) {
                Text(
                    text = item.eventTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (!item.venueName.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = item.venueName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = Corners.small,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Text(
                            text = ticket.label,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = Dimens.md, vertical = Dimens.xs)
                        )
                    }

                    Text(
                        text = "$formattedPrice per ticket",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TicketCountView(
                        count = count,
                        onIncrement = onIncrement,
                        onDecrement = onDecrement
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasketTicketCardPreview() {
    TicketWaveTheme {
        Box(modifier = Modifier.padding(Dimens.lg)) {
            BasketTicketCard(
                item = UITicketWithEvent(
                    eventId = "e1",
                    eventTitle = "Rock Night: Spring Series",
                    ticket = UITicket(
                        id = "t1",
                        label = "GA",
                        priceCents = 3696,
                        currency = "EUR",
                        quantityAvailable = 100
                    ),
                    eventImageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745",
                    venueName = "Grand Hall",
                    startsAt = LocalDateTime(2026, 9, 15, 20, 0)
                ),
                count = 2,
                onIncrement = {},
                onDecrement = {}
            )
        }
    }
}
