package nl.guidoroos.ticketwave.ui.mappers

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import nl.guidoroos.ticketwave.domain.model.Cart
import nl.guidoroos.ticketwave.ui.model.UICart
import nl.guidoroos.ticketwave.ui.model.UICartItem
import java.time.ZoneOffset
import java.time.format.DateTimeParseException
import kotlin.time.Instant

fun Cart.toUiModel(): UICart {
    return UICart(
        items = items.map { line ->
            val parsedStartsAt = line.event?.startsAt?.let {
                try {
                    Instant.parse(it).toLocalDateTime(TimeZone.UTC)
                } catch (_: Exception) {
                    null
                }
            }
            UICartItem(
                ticketId = line.ticketId,
                eventId = line.eventId,
                eventTitle = line.event?.title ?: line.eventTitle,
                ticketLabel = line.ticketLabel,
                priceCents = line.priceCents,
                currency = line.currency,
                eventImageUrl = line.eventImageUrl,
                venueName = line.event?.venue?.name,
                startsAt = parsedStartsAt
            )
        },
        creditCardNumber = creditCardNumber
    )
}
