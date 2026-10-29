package nl.guidoroos.ticketwave.domain.model

data class Event(
    val id: String,
    val title: String,
    val description: String?,
    val startsAt: String,
    val venue: Venue,
    val tickets: List<Ticket>,
    val imageUrl: String
)
