package nl.guidoroos.ticketwave.api.repository

import com.apollographql.apollo.ApolloClient
import nl.guidoroos.ticketwave.api.graphql.CheckoutMutation
import nl.guidoroos.ticketwave.api.util.safeApiCall
import nl.guidoroos.ticketwave.domain.error.NetworkError
import nl.guidoroos.ticketwave.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val apolloClient: ApolloClient
) : PaymentRepository {
    override suspend fun checkout(accessToken: String, creditCardNumber: String): Result<Unit> {
        return safeApiCall {
            val response = apolloClient.mutation(CheckoutMutation(accessToken, creditCardNumber)).execute()
            val errorText = response.data?.checkout?.error?.text
            if (!errorText.isNullOrBlank()) {
                throw NetworkError.Generic(errorText)
            }
        }
    }
}
