package nl.guidoroos.ticketwave.ui.mappers

import nl.guidoroos.ticketwave.domain.model.Ticket
import nl.guidoroos.ticketwave.ui.model.UITicket

fun Ticket.toUiModel(): UITicket {
    return UITicket(
        id = id,
        label = label,
        priceCents = priceCents,
        currency = currency,
        quantityAvailable = quantityAvailable
    )
}
