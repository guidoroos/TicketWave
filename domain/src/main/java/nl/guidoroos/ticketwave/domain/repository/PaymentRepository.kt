package nl.guidoroos.ticketwave.domain.repository

interface PaymentRepository {
    suspend fun checkout(accessToken: String, creditCardNumber: String): Result<Unit>
}
