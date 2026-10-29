package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nl.guidoroos.ticketwave.ui.theme.Dimens

@Composable
fun LoadingSpinner(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.md),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = color)
    }
}
