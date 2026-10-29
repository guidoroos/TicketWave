package nl.guidoroos.ticketwave.ui.mappers

import nl.guidoroos.ticketwave.domain.model.Venue
import nl.guidoroos.ticketwave.ui.model.UIVenue

fun Venue.toUiModel(): UIVenue {
    return UIVenue(
        id = id,
        name = name,
        imageUrl = imageUrl
    )
}
