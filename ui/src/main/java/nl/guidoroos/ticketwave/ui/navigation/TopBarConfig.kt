package nl.guidoroos.ticketwave.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import nl.guidoroos.ticketwave.ui.views.ChildTopBar
import nl.guidoroos.ticketwave.ui.views.LogoTopBar
import nl.guidoroos.ticketwave.ui.views.RootTopBar

object TopBarConfig {
    @Composable
    fun getTopBar(
        destination: NavDestination?,
        onNavigateToProfile: () -> Unit,
        onBack: () -> Unit
    ): @Composable () -> Unit {
        return when {
            destination?.hasRoute<Screen.Login>() == true -> {
                { LogoTopBar(onClickProfile = null) }
            }
            destination?.hasRoute<Screen.Home>() == true -> {
                { LogoTopBar(onClickProfile = onNavigateToProfile) }
            }
            destination?.hasRoute<Screen.Tickets>() == true -> {
                { RootTopBar(titleText = "My Tickets") }
            }
            destination?.hasRoute<Screen.Basket>() == true -> {
                { RootTopBar(titleText = "Shopping Basket") }
            }
            destination?.hasRoute<Screen.Checkout>() == true -> {
                { ChildTopBar(titleText = "Checkout", onBack = onBack) }
            }
            destination?.hasRoute<Screen.EventDetail>() == true -> {
                { ChildTopBar(titleText = "Event Detail", onBack = onBack) }
            }
            destination?.hasRoute<Screen.Barcode>() == true -> {
                { ChildTopBar(titleText = "Barcode", onBack = onBack) }
            }
            destination?.hasRoute<Screen.Setting>() == true -> {
                { ChildTopBar(titleText = "Settings", onBack = onBack) }
            }
            else -> {
                { }
            }
        }
    }
}
