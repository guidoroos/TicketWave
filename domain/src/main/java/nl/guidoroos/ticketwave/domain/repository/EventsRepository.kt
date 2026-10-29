package nl.guidoroos.ticketwave.domain.repository

import nl.guidoroos.ticketwave.domain.model.Event

interface EventsRepository {
    suspend fun events(): Result<List<Event>>
    suspend fun searchEvents(query: String): Result<List<Event>>
    suspend fun getEvent(id: String): Result<Event?>
}
