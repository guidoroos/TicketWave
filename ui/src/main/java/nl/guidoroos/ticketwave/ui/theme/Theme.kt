package nl.guidoroos.ticketwave.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Blue5,
    onPrimary = Neutral1,
    primaryContainer = Blue2,
    onPrimaryContainer = Blue7,
    secondary = Blue4,
    onSecondary = Neutral1,
    secondaryContainer = Blue1,
    onSecondaryContainer = Blue6,
    tertiary = Blue6,
    onTertiary = Neutral1,
    background = Neutral1,
    onBackground = Neutral7,
    surface = Neutral1,
    onSurface = Neutral7,
    surfaceVariant = Neutral2,
    onSurfaceVariant = Neutral5,
    error = ErrorRed,
    onError = Neutral1
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue3,
    onPrimary = Neutral7,
    primaryContainer = Blue6,
    onPrimaryContainer = Blue1,
    secondary = Blue4,
    onSecondary = Neutral7,
    secondaryContainer = Blue7,
    onSecondaryContainer = Blue2,
    tertiary = Blue3,
    onTertiary = Neutral7,
    background = Neutral7,
    onBackground = Neutral1,
    surface = Neutral6,
    onSurface = Neutral1,
    surfaceVariant = Neutral5,
    onSurfaceVariant = Neutral3,
    error = ErrorRed,
    onError = Neutral7
)

@Composable
fun TicketWaveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
