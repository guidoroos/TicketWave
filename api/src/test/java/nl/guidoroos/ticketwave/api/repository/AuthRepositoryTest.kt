package nl.guidoroos.ticketwave.api.repository

import com.apollographql.apollo.ApolloClient
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import nl.guidoroos.ticketwave.api.auth.TokenStore
import nl.guidoroos.ticketwave.api.util.safeApiCall
import nl.guidoroos.ticketwave.domain.error.NetworkError
import org.junit.Assert.*
import org.junit.Test

class AuthRepositoryTest {

    @Test
    fun testCheckAuthTrue() = runTest {
        val apolloClient = mockk<ApolloClient>(relaxed = true)
        val tokenStore = mockk<TokenStore>()
        every { tokenStore.getToken() } returns "valid-token"

        val repo = AuthRepositoryImpl(apolloClient, tokenStore)
        assertTrue(repo.checkAuth())
    }

    @Test
    fun testCheckAuthFalse() = runTest {
        val apolloClient = mockk<ApolloClient>(relaxed = true)
        val tokenStore = mockk<TokenStore>()
        every { tokenStore.getToken() } returns null

        val repo = AuthRepositoryImpl(apolloClient, tokenStore)
        assertFalse(repo.checkAuth())
    }

    @Test
    fun testGetToken() = runTest {
        val apolloClient = mockk<ApolloClient>(relaxed = true)
        val tokenStore = mockk<TokenStore>()
        every { tokenStore.getToken() } returns "my-secret-token"

        val repo = AuthRepositoryImpl(apolloClient, tokenStore)
        assertEquals("my-secret-token", repo.getToken())
    }

    @Test
    fun testLoginSuccessData() = runTest {
        val result = safeApiCall { "success-token" }
        assertTrue(result.isSuccess)
        assertEquals("success-token", result.getOrNull())
    }

    @Test
    fun testLoginEmptyResponse() = runTest {
        val result = safeApiCall { null }
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun testLoginErrorResponse() = runTest {
        val result = safeApiCall { throw NetworkError.Generic("Invalid credentials") }
        assertTrue(result.isFailure)
        assertEquals("Invalid credentials", result.exceptionOrNull()?.message)
    }
}
