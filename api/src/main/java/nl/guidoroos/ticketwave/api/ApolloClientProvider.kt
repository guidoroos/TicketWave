package nl.guidoroos.ticketwave.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.network.http.DefaultHttpEngine
import com.apollographql.apollo.network.http.HttpNetworkTransport
import okhttp3.OkHttpClient
import nl.guidoroos.ticketwave.api.auth.AuthInterceptor
import nl.guidoroos.ticketwave.api.auth.ErrorHandlingInterceptor

object ApolloClientProvider {
    fun provideApolloClient(
        authInterceptor: AuthInterceptor,
        errorHandlingInterceptor: ErrorHandlingInterceptor
    ): ApolloClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(errorHandlingInterceptor)
            .addInterceptor(LoggingInterceptor())
            .build()

        val networkTransport = HttpNetworkTransport.Builder()
            .serverUrl("http://localhost:4000/graphql")
            .httpEngine(DefaultHttpEngine { okHttpClient })
            .build()

        val cacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)

        return ApolloClient.Builder()
            .networkTransport(networkTransport)
            .normalizedCache(cacheFactory)
            .fetchPolicy(FetchPolicy.CacheFirst)
            .build()
    }
}
