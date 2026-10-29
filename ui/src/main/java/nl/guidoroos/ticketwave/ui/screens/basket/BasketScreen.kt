package nl.guidoroos.ticketwave.ui.screens.basket

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.model.UITicket
import nl.guidoroos.ticketwave.ui.model.UITicketWithEvent
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.theme.TicketWaveTheme
import nl.guidoroos.ticketwave.ui.views.BasketTicketCard
import nl.guidoroos.ticketwave.ui.views.CreditCardBottomSheet
import nl.guidoroos.ticketwave.ui.views.LoadingSpinner
import nl.guidoroos.ticketwave.ui.views.PaymentMethodCard
import nl.guidoroos.ticketwave.ui.views.PrimaryButton
import nl.guidoroos.ticketwave.ui.views.SecondaryButton
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun BasketScreen(
    modifier: Modifier = Modifier,
    onEvent: (AppEvent) -> Unit = {},
    onNavigateToCheckout: () -> Unit = {},
    onContinueShopping: () -> Unit = {},
    viewModel: BasketViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCardSheet by remember { mutableStateOf(false) }

    BasketScreenView(
        modifier = modifier,
        uiState = uiState,
        onIncrement = { ticketId -> viewModel.increment(ticketId, onEvent) },
        onDecrement = { ticketId -> viewModel.decrement(ticketId, onEvent) },
        onChangeCard = { showCardSheet = true },
        onCheckoutClick = onNavigateToCheckout,
        onContinueShoppingClick = onContinueShopping
    )

    if (showCardSheet) {
        CreditCardBottomSheet(
            onDismiss = { showCardSheet = false },
            onSave = { cardNumber ->
                viewModel.saveCreditCard(
                    cardNumber = cardNumber,
                    onEvent = onEvent,
                    onSuccess = { showCardSheet = false }
                )
            }
        )
    }
}

@Composable
fun BasketScreenView(
    uiState: BasketState,
    onIncrement: (String) -> Unit,
    onDecrement: (String) -> Unit,
    onChangeCard: () -> Unit,
    onCheckoutClick: () -> Unit,
    onContinueShoppingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading && uiState.items.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingSpinner()
        }
    } else if (uiState.items.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(Dimens.lg),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.md)
            ) {
                Text(
                    text = "Your basket is empty",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SecondaryButton(
                    text = "Continue shopping",
                    onClick = onContinueShoppingClick
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(Dimens.lg),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.md)
            ) {
                items(uiState.items, key = { it.item.ticket.id }) { basketItem ->
                    BasketTicketCard(
                        item = basketItem.item,
                        count = basketItem.count,
                        onIncrement = { onIncrement(basketItem.item.ticket.id) },
                        onDecrement = { onDecrement(basketItem.item.ticket.id) }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.md),
                verticalArrangement = Arrangement.spacedBy(Dimens.md)
            ) {
                HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.xs))

                PaymentMethodCard(
                    creditCardNumber = uiState.creditCardNumber ?: "",
                    onChangeCardClick = onChangeCard
                )

                Spacer(modifier = Modifier.height(Dimens.xs))

                val formattedTotal = String.format(Locale.ROOT, "€%.2f", uiState.totalPriceCents / 100.0)
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
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.xs))

                PrimaryButton(
                    text = "Go to checkout",
                    onClick = onCheckoutClick
                )

                SecondaryButton(
                    text = "Continue shopping",
                    onClick = onContinueShoppingClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasketScreenViewPreview() {
    TicketWaveTheme {
        BasketScreenView(
            uiState = BasketState(
                items = listOf(
                    BasketItemState(
                        item = UITicketWithEvent(
                            eventId = "e1",
                            eventTitle = "Rock Night: Spring Series",
                            ticket = UITicket("t1", "GA", 3696, "EUR", 100),
                            eventImageUrl = ""
                        ),
                        count = 2
                    )
                )
            ),
            onIncrement = {},
            onDecrement = {},
            onChangeCard = {},
            onCheckoutClick = {},
            onContinueShoppingClick = {}
        )
    }
}
