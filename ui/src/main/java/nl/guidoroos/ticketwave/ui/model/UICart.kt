package nl.guidoroos.ticketwave.ui.model

import kotlinx.datetime.LocalDateTime

data class UICart(
    val items: List<UICartItem>,
    val creditCardNumber: String?
)

data class UICartItem(
    val ticketId: String,
    val eventId: String,
    val eventTitle: String,
    val ticketLabel: String,
    val priceCents: Int,
    val currency: String,
    val eventImageUrl: String,
    val venueName: String?,
    val startsAt: LocalDateTime?
)
