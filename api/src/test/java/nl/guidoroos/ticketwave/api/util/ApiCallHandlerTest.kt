package nl.guidoroos.ticketwave.api.util

import kotlinx.coroutines.test.runTest
import nl.guidoroos.ticketwave.domain.error.NetworkError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ApiCallHandlerTest {

    @Test
    fun testSafeApiCallSuccess() = runTest {
        val result = safeApiCall { "Success Data" }
        assertTrue(result.isSuccess)
        assertEquals("Success Data", result.getOrNull())
    }

    @Test
    fun testSafeApiCallEmpty() = runTest {
        val result = safeApiCall { null }
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun testSafeApiCallError() = runTest {
        val result = safeApiCall { throw NetworkError.Generic("Server error") }
        assertTrue(result.isFailure)
        assertEquals("Server error", result.exceptionOrNull()?.message)
    }

    @Test
    fun testSafeApiCallIoException() = runTest {
        val result = safeApiCall { throw IOException("Connection lost") }
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NetworkError.NoInternet)
    }
}
