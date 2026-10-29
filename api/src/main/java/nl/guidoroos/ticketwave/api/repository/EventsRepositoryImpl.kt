package nl.guidoroos.ticketwave.api.repository

import com.apollographql.apollo.ApolloClient
import nl.guidoroos.ticketwave.api.graphql.EventQuery
import nl.guidoroos.ticketwave.api.graphql.EventsQuery
import nl.guidoroos.ticketwave.api.graphql.SearchEventsQuery
import nl.guidoroos.ticketwave.api.util.safeApiCall
import nl.guidoroos.ticketwave.domain.error.NetworkError
import nl.guidoroos.ticketwave.domain.model.Event
import nl.guidoroos.ticketwave.domain.model.Ticket
import nl.guidoroos.ticketwave.domain.model.Venue
import nl.guidoroos.ticketwave.domain.repository.EventsRepository

class EventsRepositoryImpl(
    private val apolloClient: ApolloClient
) : EventsRepository {

    override suspend fun events(): Result<List<Event>> {
        return safeApiCall {
            val response = apolloClient.query(EventsQuery()).execute()
            val data = response.data?.events
            val errorText = data?.error?.text

            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }

            data?.events?.map { apolloEvent ->
                Event(
                    id = apolloEvent.id,
                    title = apolloEvent.title,
                    description = apolloEvent.description,
                    startsAt = apolloEvent.startsAt,
                    venue = Venue(
                        id = apolloEvent.venue.id,
                        name = apolloEvent.venue.name,
                        imageUrl = apolloEvent.venue.imageUrl
                    ),
                    tickets = apolloEvent.tickets.map { apolloTicket ->
                        Ticket(
                            id = apolloTicket.id,
                            label = apolloTicket.label,
                            priceCents = apolloTicket.priceCents,
                            currency = apolloTicket.currency,
                            quantityAvailable = apolloTicket.quantityAvailable
                        )
                    },
                    imageUrl = apolloEvent.imageUrl
                )
            } ?: emptyList()
        }
    }

    override suspend fun searchEvents(query: String): Result<List<Event>> {
        return safeApiCall {
            val response = apolloClient.query(SearchEventsQuery(query)).execute()
            val data = response.data?.searchEvents
            val errorText = data?.error?.text

            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }

            data?.events?.map { apolloEvent ->
                Event(
                    id = apolloEvent.id,
                    title = apolloEvent.title,
                    description = apolloEvent.description,
                    startsAt = apolloEvent.startsAt,
                    venue = Venue(
                        id = apolloEvent.venue.id,
                        name = apolloEvent.venue.name,
                        imageUrl = apolloEvent.venue.imageUrl
                    ),
                    tickets = apolloEvent.tickets.map { apolloTicket ->
                        Ticket(
                            id = apolloTicket.id,
                            label = apolloTicket.label,
                            priceCents = apolloTicket.priceCents,
                            currency = apolloTicket.currency,
                            quantityAvailable = apolloTicket.quantityAvailable
                        )
                    },
                    imageUrl = apolloEvent.imageUrl
                )
            } ?: emptyList()
        }
    }

    override suspend fun getEvent(id: String): Result<Event?> {
        return safeApiCall {
            val response = apolloClient.query(EventQuery(id)).execute()
            val data = response.data?.event
            val errorText = data?.error?.text

            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }

            val apolloEvent = data?.event ?: return@safeApiCall null
            Event(
                id = apolloEvent.id,
                title = apolloEvent.title,
                description = apolloEvent.description,
                startsAt = apolloEvent.startsAt,
                venue = Venue(
                    id = apolloEvent.venue.id,
                    name = apolloEvent.venue.name,
                    imageUrl = apolloEvent.venue.imageUrl
                ),
                tickets = apolloEvent.tickets.map { apolloTicket ->
                    Ticket(
                        id = apolloTicket.id,
                        label = apolloTicket.label,
                        priceCents = apolloTicket.priceCents,
                        currency = apolloTicket.currency,
                        quantityAvailable = apolloTicket.quantityAvailable
                    )
                },
                imageUrl = apolloEvent.imageUrl
            )
        }
    }
}
