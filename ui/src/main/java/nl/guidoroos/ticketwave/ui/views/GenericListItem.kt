package nl.guidoroos.ticketwave.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nl.guidoroos.ticketwave.ui.theme.Dimens

@Composable
fun GenericListItem(
    start: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    end: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    },
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Row(
        modifier = clickableModifier
            .fillMaxWidth()
            .padding(Dimens.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.md)
    ) {
        Box(contentAlignment = Alignment.Center) {
            start()
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            content()
        }
        Box(contentAlignment = Alignment.Center) {
            end()
        }
    }
}
