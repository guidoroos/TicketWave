package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import nl.guidoroos.ticketwave.ui.theme.Dimens
import nl.guidoroos.ticketwave.ui.utils.isValidCreditCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var cardNumberInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.lg),
            verticalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            Text(
                text = "Update Credit Card",
                style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(
                value = cardNumberInput,
                onValueChange = {
                    cardNumberInput = it
                    errorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter card number") },
                singleLine = true,
                isError = errorMessage != null,
                supportingText = {
                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            PrimaryButton(
                text = "Save card",
                onClick = {
                    if (isValidCreditCard(cardNumberInput)) {
                        onSave(cardNumberInput)
                    } else {
                        errorMessage = "Please enter a valid credit card number."
                    }
                }
            )
            Spacer(modifier = Modifier.height(Dimens.xl))
        }
    }
}
