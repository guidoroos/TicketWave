package nl.guidoroos.ticketwave.api.repository

import com.apollographql.apollo.ApolloClient
import nl.guidoroos.ticketwave.api.graphql.AddTicketToCartMutation
import nl.guidoroos.ticketwave.api.graphql.AuthenticatedUserMutation
import nl.guidoroos.ticketwave.api.graphql.RemoveFromCartMutation
import nl.guidoroos.ticketwave.api.graphql.SaveCreditCardMutation
import nl.guidoroos.ticketwave.api.util.safeApiCall
import nl.guidoroos.ticketwave.domain.error.NetworkError
import nl.guidoroos.ticketwave.domain.model.Cart
import nl.guidoroos.ticketwave.domain.model.CartItem
import nl.guidoroos.ticketwave.domain.model.OwnedTicket
import nl.guidoroos.ticketwave.domain.model.UserProfile
import nl.guidoroos.ticketwave.domain.repository.EventsRepository
import nl.guidoroos.ticketwave.domain.repository.UserRepository

class UserRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val eventsRepository: EventsRepository
) : UserRepository {

    override suspend fun getCart(accessToken: String): Result<Cart> {
        return safeApiCall {
            val response = apolloClient.mutation(AuthenticatedUserMutation(accessToken)).execute()
            val data = response.data?.authenticatedUser
            val errorText = data?.error?.text
            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }
            val user = data?.user
            val cartLines = user?.cart?.map { line ->
                val eventResult = eventsRepository.getEvent(line.eventId)
                val event = eventResult.getOrNull()
                val imageUrl = event?.imageUrl ?: ""
                CartItem(
                    ticketId = line.ticketId,
                    eventId = line.eventId,
                    eventTitle = event?.title ?: line.eventTitle,
                    ticketLabel = line.ticketLabel,
                    priceCents = line.priceCents,
                    currency = line.currency,
                    eventImageUrl = imageUrl,
                    event = event
                )
            } ?: emptyList()
            val creditCard = user?.creditCardNumber
            Cart(items = cartLines, creditCardNumber = creditCard)
        }
    }

    override suspend fun getUserProfile(accessToken: String): Result<UserProfile> {
        return safeApiCall {
            val response = apolloClient.mutation(AuthenticatedUserMutation(accessToken)).execute()
            val data = response.data?.authenticatedUser
            val errorText = data?.error?.text
            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }
            val user = data?.user
            UserProfile(
                firstName = user?.firstName ?: "",
                lastName = user?.lastName ?: "",
                age = user?.age ?: 0,
                creditCardNumber = user?.creditCardNumber
            )
        }
    }

    override suspend fun getTicketWallet(accessToken: String): Result<List<OwnedTicket>> {
        return safeApiCall {
            val response = apolloClient.mutation(AuthenticatedUserMutation(accessToken)).execute()
            val data = response.data?.authenticatedUser
            val errorText = data?.error?.text
            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }
            val eventsResult = eventsRepository.events()
            val events = eventsResult.getOrNull() ?: emptyList()

            data?.user?.ticketWallet?.map { owned ->
                var matchedEvent: nl.guidoroos.ticketwave.domain.model.Event? = null
                for (event in events) {
                    if (event.tickets.any { it.id == owned.id || (it.label == owned.label && it.priceCents == owned.priceCents) }) {
                        matchedEvent = event
                        break
                    }
                }

                OwnedTicket(
                    id = owned.id,
                    label = owned.label,
                    priceCents = owned.priceCents,
                    currency = owned.currency,
                    barcode = owned.barcode,
                    eventId = matchedEvent?.id,
                    eventTitle = matchedEvent?.title ?: "Concert Ticket",
                    eventImageUrl = matchedEvent?.imageUrl ?: "",
                    venueName = matchedEvent?.venue?.name,
                    startsAt = matchedEvent?.startsAt
                )
            } ?: emptyList()
        }
    }

    override suspend fun addTicketToCart(accessToken: String, ticketId: String): Result<Unit> {
        return safeApiCall {
            val response = apolloClient.mutation(AddTicketToCartMutation(accessToken, ticketId)).execute()
            val errorText = response.data?.addTicketToCart?.error?.text
            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }
        }
    }

    override suspend fun removeTicketFromCart(accessToken: String, ticketId: String): Result<Unit> {
        return safeApiCall {
            val response = apolloClient.mutation(RemoveFromCartMutation(accessToken, ticketId)).execute()
            val errorText = response.data?.removeTicketFromCart?.error?.text
            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }
        }
    }

    override suspend fun saveCreditCard(accessToken: String, creditCardNumber: String): Result<Unit> {
        return safeApiCall {
            val response = apolloClient.mutation(SaveCreditCardMutation(accessToken, creditCardNumber)).execute()
            val errorText = response.data?.saveCreditCard?.error?.text
            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }
        }
    }
}
