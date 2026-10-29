package nl.guidoroos.ticketwave.ui.screens.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.views.CreditCardBottomSheet
import nl.guidoroos.ticketwave.ui.views.LoadingSpinner
import nl.guidoroos.ticketwave.ui.views.PaymentMethodCard
import nl.guidoroos.ticketwave.ui.views.PrimaryButton
import nl.guidoroos.ticketwave.ui.views.TicketSummaryView
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    onEvent: (AppEvent) -> Unit = {},
    onCheckoutSuccess: () -> Unit = {},
    viewModel: CheckoutViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCardSheet by remember { mutableStateOf(false) }

    if (uiState.isLoading && uiState.items.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingSpinner()
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.lg),
            verticalArrangement = Arrangement.spacedBy(Dimens.lg)
        ) {
            TicketSummaryView(
                items = uiState.items,
                totalPriceCents = uiState.totalPriceCents
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.xs))

            PaymentMethodCard(
                creditCardNumber = uiState.creditCardNumber ?: "",
                onChangeCardClick = { showCardSheet = true }
            )

            Spacer(modifier = Modifier.height(Dimens.md))

            PrimaryButton(
                text = "Confirm payment",
                onClick = {
                    viewModel.checkout(onEvent, onCheckoutSuccess)
                }
            )
        }
    }

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
