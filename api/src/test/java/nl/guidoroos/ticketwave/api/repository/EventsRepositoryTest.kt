package nl.guidoroos.ticketwave.api.repository

import kotlinx.coroutines.test.runTest
import nl.guidoroos.ticketwave.api.util.safeApiCall
import nl.guidoroos.ticketwave.domain.error.NetworkError
import org.junit.Assert.*
import org.junit.Test

class EventsRepositoryTest {

    @Test
    fun testGetEventsSuccess() = runTest {
        val result = safeApiCall { emptyList<nl.guidoroos.ticketwave.domain.model.Event>() }
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull().orEmpty().isEmpty())
    }

    @Test
    fun testGetEventByIdEmpty() = runTest {
        val result = safeApiCall { null as nl.guidoroos.ticketwave.domain.model.Event? }
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun testSearchEventsError() = runTest {
        val result = safeApiCall { throw NetworkError.Generic("Search failed") }
        assertTrue(result.isFailure)
        assertEquals("Search failed", result.exceptionOrNull()?.message)
    }
}
