package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.guidoroos.ticketwave.ui.model.UIOwnedTicketGroup
import nl.guidoroos.ticketwave.ui.theme.Corners
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.theme.WarningOrange
import nl.guidoroos.ticketwave.ui.utils.formatCurrency
import nl.guidoroos.ticketwave.ui.utils.formatEventDate
import java.util.Locale

@Composable
fun TicketPurchaseView(
    group: UIOwnedTicketGroup,
    onShowBarcode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedTotal = formatCurrency(group.totalPriceCents, group.currency)

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
            if (group.eventImageUrl.isNotBlank() || group.startsAt != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    EventAsyncImage(
                        imageUrl = group.eventImageUrl,
                        contentDescription = group.eventTitle,
                        height = 140.dp
                    )

                    group.startsAt?.let { startsAt ->
                        Surface(
                            modifier = Modifier
                                .padding(Dimens.md)
                                .align(Alignment.TopStart),
                            shape = Corners.small,
                            color = WarningOrange,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(
                                text = formatEventDate(startsAt),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(
                                    horizontal = Dimens.md,
                                    vertical = Dimens.sm
                                )
                            )
                        }
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
                    text = group.eventTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (!group.venueName.isNullOrBlank()) {
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
                            text = group.venueName,
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
                            text = group.ticketLabel,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = Dimens.md, vertical = Dimens.xs)
                        )
                    }

                    Text(
                        text = "${group.count} ticket${if (group.count > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Paid",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formattedTotal,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (group.barcodes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(Dimens.xs))
                    group.barcodes.forEachIndexed { index, barcode ->
                        if (index > 0) Spacer(modifier = Modifier.height(Dimens.sm))
                        SecondaryButton(
                            text = if (group.barcodes.size > 1) "Show barcode #${index + 1}" else "Show barcode",
                            onClick = { onShowBarcode(barcode) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
