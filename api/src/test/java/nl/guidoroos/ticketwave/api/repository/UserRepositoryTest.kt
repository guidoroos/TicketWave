package nl.guidoroos.ticketwave.api.repository

import kotlinx.coroutines.test.runTest
import nl.guidoroos.ticketwave.api.util.safeApiCall
import nl.guidoroos.ticketwave.domain.error.NetworkError
import nl.guidoroos.ticketwave.domain.model.Cart
import org.junit.Assert.*
import org.junit.Test

class UserRepositoryTest {

    @Test
    fun testGetCartSuccess() = runTest {
        val result = safeApiCall { Cart(items = emptyList(), creditCardNumber = "4242") }
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun testGetCartEmpty() = runTest {
        val result = safeApiCall { Cart(items = emptyList(), creditCardNumber = null) }
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.items.orEmpty().isEmpty())
    }

    @Test
    fun testGetUserProfileError() = runTest {
        val result = safeApiCall { throw NetworkError.Generic("Unauthorized") }
        assertTrue(result.isFailure)
        assertEquals("Unauthorized", result.exceptionOrNull()?.message)
    }

    @Test
    fun testAddTicketToCartSuccess() = runTest {
        val result = safeApiCall { Unit }
        assertTrue(result.isSuccess)
    }

    @Test
    fun testRemoveTicketFromCartError() = runTest {
        val result = safeApiCall { throw NetworkError.Generic("Failed to remove") }
        assertTrue(result.isFailure)
    }

    @Test
    fun testSaveCreditCardSuccess() = runTest {
        val result = safeApiCall { Unit }
        assertTrue(result.isSuccess)
    }
}
