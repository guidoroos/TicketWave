package nl.guidoroos.ticketwave.ui.screens.setting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.theme.Corners
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.views.CreditCardBottomSheet
import nl.guidoroos.ticketwave.ui.views.GenericListItem
import nl.guidoroos.ticketwave.ui.views.LoadingSpinner
import nl.guidoroos.ticketwave.ui.views.PrimaryButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onEvent: (AppEvent) -> Unit = {},
    onNavigateToTickets: () -> Unit = {},
    onLogoutSuccess: () -> Unit = {},
    viewModel: SettingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCardSheet by remember { mutableStateOf(false) }

    if (uiState.isLoading && uiState.fullName.isBlank()) {
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
            verticalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                GenericListItem(
                    start = {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    },
                    content = {
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens.xs)) {
                            Text(
                                text = uiState.fullName.ifBlank { "User" },
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (uiState.age > 0) {
                                Text(
                                    text = "${uiState.age} years old",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    end = {}
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                val cardNum = uiState.creditCardNumber
                val displayCard = if (!cardNum.isNullOrBlank() && cardNum.length > 4) {
                    "Visa •••• ${cardNum.takeLast(4)}"
                } else {
                    "No credit card added"
                }

                GenericListItem(
                    start = {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = Corners.small,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CreditCard,
                                    contentDescription = "Payment",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    },
                    content = {
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens.xs)) {
                            Text(
                                text = "Payment Details",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = displayCard,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = { showCardSheet = true }
                )
            }

            // My Tickets Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                GenericListItem(
                    start = {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = Corners.small,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.List,
                                    contentDescription = "Tickets",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    },
                    content = {
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens.xs)) {
                            Text(
                                text = "My Tickets",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "View and manage your tickets",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = onNavigateToTickets
                )
            }

            Spacer(modifier = Modifier.height(Dimens.lg))

            // Logout Button
            PrimaryButton(
                text = "Logout",
                onClick = {
                    viewModel.logout(onEvent, onLogoutSuccess)
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
