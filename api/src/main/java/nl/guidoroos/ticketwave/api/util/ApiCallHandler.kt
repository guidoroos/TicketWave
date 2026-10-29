package nl.guidoroos.ticketwave.api.util

import com.apollographql.apollo.exception.ApolloNetworkException
import nl.guidoroos.ticketwave.domain.error.NetworkError
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: NetworkError) {
        Result.failure(e)
    } catch (e: ApolloNetworkException) {
        Result.failure(NetworkError.NoInternet())
    } catch (e: IOException) {
        Result.failure(NetworkError.NoInternet())
    } catch (e: Exception) {
        Result.failure(NetworkError.Generic(e.message ?: "An unexpected error occurred"))
    }
}
