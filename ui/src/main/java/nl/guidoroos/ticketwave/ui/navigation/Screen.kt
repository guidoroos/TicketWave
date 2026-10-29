package nl.guidoroos.ticketwave.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Tickets : Screen

    @Serializable
    data object Basket : Screen

    @Serializable
    data object Checkout : Screen

    @Serializable
    data class EventDetail(val eventId: String) : Screen

    @Serializable
    data class Barcode(val barcodeNumber: String) : Screen

    @Serializable
    data object Setting : Screen
}
