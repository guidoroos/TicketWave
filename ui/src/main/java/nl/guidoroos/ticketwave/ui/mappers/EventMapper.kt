package nl.guidoroos.ticketwave.ui.mappers

import kotlinx.datetime.LocalDateTime
import nl.guidoroos.ticketwave.domain.model.Event
import nl.guidoroos.ticketwave.ui.model.UIEvent

fun Event.toUiModel(): UIEvent {
    val parsedDateTime = try {
        LocalDateTime.parse(startsAt.removeSuffix("Z"))
    } catch (_: Exception) {
       null
    }

    return UIEvent(
        id = id,
        title = title,
        description = description,
        startsAt = parsedDateTime,
        venue = venue.toUiModel(),
        tickets = tickets.map { it.toUiModel() },
        imageUrl = imageUrl
    )
}
