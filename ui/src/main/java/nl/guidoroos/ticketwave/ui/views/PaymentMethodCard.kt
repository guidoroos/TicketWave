package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.guidoroos.ticketwave.ui.theme.Corners
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.theme.TicketWaveTheme

@Composable
fun PaymentMethodCard(
    creditCardNumber: String,
    onChangeCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val maskedNumber = if (creditCardNumber.isNotBlank()) {
        if (creditCardNumber.length > 4) {
            "•••• •••• •••• ${creditCardNumber.takeLast(4)}"
        } else {
            creditCardNumber
        }
    } else {
        "No credit card added"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Corners.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.lg),
            verticalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            Text(
                text = "Card for payment",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = maskedNumber,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedButton(
                    onClick = onChangeCardClick,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Change card",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentMethodCardPreview() {
    TicketWaveTheme {
        Box(modifier = Modifier.padding(Dimens.lg)) {
            PaymentMethodCard(
                creditCardNumber = "4242424242424242",
                onChangeCardClick = {}
            )
        }
    }
}
