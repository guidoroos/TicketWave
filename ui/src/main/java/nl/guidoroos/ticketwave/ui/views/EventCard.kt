package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import nl.guidoroos.ticketwave.ui.model.UIEvent
import nl.guidoroos.ticketwave.ui.model.UITicket
import nl.guidoroos.ticketwave.ui.model.UIVenue
import nl.guidoroos.ticketwave.ui.theme.Corners
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.theme.TicketWaveTheme
import nl.guidoroos.ticketwave.ui.theme.WarningOrange
import nl.guidoroos.ticketwave.ui.utils.formatCurrency
import nl.guidoroos.ticketwave.ui.utils.formatEventDate

@Composable
fun EventCard(
    event: UIEvent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = Corners.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image with date chip overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                EventAsyncImage(
                    imageUrl = event.imageUrl,
                    contentDescription = event.title,
                    height = 200.dp
                )

                event.startsAt?.let {
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
                    .padding(Dimens.xl),
                verticalArrangement = Arrangement.spacedBy(Dimens.lg)
            ) {
                // Big Title text
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Description with readable size
                if (!event.description.isNullOrBlank()) {
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3
                    )
                }

                // Row: Location icon, venue title text
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
                        text = event.venue.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Tickets from min price
                val minTicket = event.tickets.minByOrNull { it.priceCents }
                val minPriceCents = minTicket?.priceCents ?: 0
                val currency = minTicket?.currency ?: "EUR"
                val formattedPrice = formatCurrency(minPriceCents, currency)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalActivity,
                        contentDescription = "Tickets",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Tickets from $formattedPrice",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Horizontal scrollable list for chips
                if (event.tickets.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
                    ) {
                        items(event.tickets) { ticket ->
                            Surface(
                                shape = Corners.small,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ) {
                                Text(
                                    text = ticket.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = Dimens.md, vertical = Dimens.xs),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardPreview() {
    TicketWaveTheme {
        Box(modifier = Modifier.padding(Dimens.lg)) {
            EventCard(
                event = UIEvent(
                    id = "1",
                    title = "Coldplay World Tour 2026",
                    description = "Experience the magic live in concert with state-of-the-art visual effects and sound.",
                    startsAt = LocalDateTime(2026, 9, 15, 20, 0),
                    venue = UIVenue("v1", "Johan Cruijff ArenA, Amsterdam", "url"),
                    tickets = listOf(
                        UITicket("t1", "Early Bird", 4500, "EUR", 50),
                        UITicket("t2", "Standard", 6500, "EUR", 120),
                        UITicket("t3", "VIP (Early Release)", 12500, "EUR", 30),
                        UITicket("t4", "Balcony Seat", 8500, "EUR", 75)
                    ),
                    imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745"
                ),
                onClick = {}
            )
        }
    }
}
