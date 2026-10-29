package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.guidoroos.ticketwave.ui.model.UITicket
import nl.guidoroos.ticketwave.ui.theme.Corners
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.theme.TicketWaveTheme
import nl.guidoroos.ticketwave.ui.utils.formatCurrency

@Composable
fun TicketCard(
    ticket: UITicket,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Corners.medium
) {
    val formattedPrice = formatCurrency(ticket.priceCents, ticket.currency)

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            Icon(
                imageVector = Icons.Default.LocalActivity,
                contentDescription = "Ticket",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = ticket.label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Select",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketCardPreview() {
    TicketWaveTheme {
        Box(modifier = Modifier.padding(Dimens.lg)) {
            TicketCard(
                ticket = UITicket(
                    id = "t1",
                    label = "VIP Access & Meet and Greet",
                    priceCents = 12500,
                    currency = "EUR",
                    quantityAvailable = 25
                ),
                onClick = {}
            )
        }
    }
}
