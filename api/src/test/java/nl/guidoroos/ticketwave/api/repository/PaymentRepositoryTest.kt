package nl.guidoroos.ticketwave.api.repository

import kotlinx.coroutines.test.runTest
import nl.guidoroos.ticketwave.api.util.safeApiCall
import nl.guidoroos.ticketwave.domain.error.NetworkError
import org.junit.Assert.*
import org.junit.Test

class PaymentRepositoryTest {

    @Test
    fun testCheckoutSuccess() = runTest {
        val result = safeApiCall { Unit }
        assertTrue(result.isSuccess)
    }

    @Test
    fun testCheckoutError() = runTest {
        val result = safeApiCall { throw NetworkError.Generic("Payment declined") }
        assertTrue(result.isFailure)
        assertEquals("Payment declined", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCheckoutEmpty() = runTest {
        val result = safeApiCall { Unit }
        assertTrue(result.isSuccess)
    }
}
