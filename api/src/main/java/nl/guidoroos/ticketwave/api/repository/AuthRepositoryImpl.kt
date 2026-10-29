package nl.guidoroos.ticketwave.api.repository

import com.apollographql.apollo.ApolloClient
import nl.guidoroos.ticketwave.api.auth.TokenStore
import nl.guidoroos.ticketwave.api.graphql.LoginMutation
import nl.guidoroos.ticketwave.api.graphql.LogoutMutation
import nl.guidoroos.ticketwave.api.util.safeApiCall
import nl.guidoroos.ticketwave.domain.error.NetworkError
import nl.guidoroos.ticketwave.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val tokenStore: TokenStore
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Unit> {
        return safeApiCall {
            val response = apolloClient.mutation(LoginMutation(username, password)).execute()
            val data = response.data?.login
            val errorText = data?.error?.text
            val accessToken = data?.accessToken

            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            } else if (!accessToken.isNullOrBlank()) {
                tokenStore.setToken(accessToken)
            } else {
                throw NetworkError.Generic("Unknown login error occurred")
            }
        }
    }

    override suspend fun logout(): Result<Boolean> {
        return safeApiCall {
            val token = tokenStore.getToken()
            if (!token.isNullOrBlank()) {
                val response = apolloClient.mutation(LogoutMutation(token)).execute()
                val errorText = response.data?.logout?.error?.text
                if (!errorText.isNullOrBlank()) {
                    tokenStore.setToken(null)
                    throw NetworkError.Generic(errorText)
                }
            }
            tokenStore.setToken(null)
            true
        }
    }

    override suspend fun checkAuth(): Boolean {
        return !tokenStore.getToken().isNullOrBlank()
    }

    override suspend fun getToken(): String? {
        return tokenStore.getToken()
    }
}
