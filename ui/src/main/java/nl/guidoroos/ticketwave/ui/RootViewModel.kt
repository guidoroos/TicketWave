package nl.guidoroos.ticketwave.ui

import androidx.lifecycle.ViewModel
import nl.guidoroos.ticketwave.api.auth.TokenStore
import nl.guidoroos.ticketwave.ui.navigation.Screen

class RootViewModel(
    tokenStore: TokenStore
) : ViewModel() {
    val startDestination: Screen = if (!tokenStore.getToken().isNullOrBlank()) {
        Screen.Home
    } else {
        Screen.Login
    }
}
