package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import nl.guidoroos.ticketwave.ui.screens.basket.BasketItemState
import nl.guidoroos.ticketwave.ui.theme.Corners
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.utils.formatCurrency
import nl.guidoroos.ticketwave.ui.utils.formatEventDate

@Composable
fun TicketSummaryView(
    items: List<BasketItemState>,
    totalPriceCents: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.lg),
            verticalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            Text(
                text = "Order overview",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            items.forEachIndexed { index, basketItem ->
                val ticket = basketItem.item.ticket
                val unitPrice = ticket.priceCents / 100.0
                val lineTotal = unitPrice * basketItem.count
                val formattedUnitPrice = formatCurrency(ticket.priceCents, ticket.currency)
                val formattedLineTotal = formatCurrency((lineTotal * 100).toInt(), ticket.currency)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = basketItem.item.eventImageUrl,
                        contentDescription = basketItem.item.eventTitle,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(Corners.small),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(Dimens.xs)
                    ) {
                        Text(
                            text = basketItem.item.eventTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (!basketItem.item.venueName.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Dimens.xs)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = basketItem.item.venueName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        basketItem.item.startsAt?.let { startsAt ->
                            Text(
                                text = formatEventDate(startsAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = Corners.small,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Text(
                                text = ticket.label,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = Dimens.sm, vertical = 2.dp)
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(Dimens.xs)
                    ) {
                        Text(
                            text = "${basketItem.count} × $formattedUnitPrice",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formattedLineTotal,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (index < items.size - 1) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.xs))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.xs))

            val formattedTotal = formatCurrency(totalPriceCents, items.firstOrNull()?.item?.ticket?.currency ?: "EUR")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formattedTotal,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
