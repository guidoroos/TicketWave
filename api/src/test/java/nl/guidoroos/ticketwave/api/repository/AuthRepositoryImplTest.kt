package nl.guidoroos.ticketwave.api.repository

import com.apollographql.apollo.ApolloClient
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import nl.guidoroos.ticketwave.api.auth.TokenStore
import org.junit.Assert.*
import org.junit.Test

class AuthRepositoryImplTest {

    @Test
    fun testCheckAuthTrue() = runTest {
        val apolloClient = mockk<ApolloClient>(relaxed = true)
        val tokenStore = mockk<TokenStore>()
        every { tokenStore.getToken() } returns "token"
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
        every { tokenStore.getToken() } returns "my-token"
        val repo = AuthRepositoryImpl(apolloClient, tokenStore)
        assertEquals("my-token", repo.getToken())
    }
}
