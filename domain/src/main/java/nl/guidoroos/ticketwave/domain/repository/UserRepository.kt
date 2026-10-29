package nl.guidoroos.ticketwave.domain.repository

import nl.guidoroos.ticketwave.domain.model.Cart
import nl.guidoroos.ticketwave.domain.model.OwnedTicket
import nl.guidoroos.ticketwave.domain.model.UserProfile

interface UserRepository {
    suspend fun getCart(accessToken: String): Result<Cart>
    suspend fun getUserProfile(accessToken: String): Result<UserProfile>
    suspend fun getTicketWallet(accessToken: String): Result<List<OwnedTicket>>
    suspend fun addTicketToCart(accessToken: String, ticketId: String): Result<Unit>
    suspend fun removeTicketFromCart(accessToken: String, ticketId: String): Result<Unit>
    suspend fun saveCreditCard(accessToken: String, creditCardNumber: String): Result<Unit>
}
