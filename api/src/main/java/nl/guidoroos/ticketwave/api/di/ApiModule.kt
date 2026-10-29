package nl.guidoroos.ticketwave.api.di

import com.apollographql.apollo.ApolloClient
import nl.guidoroos.ticketwave.api.ApolloClientProvider
import nl.guidoroos.ticketwave.api.auth.AuthInterceptor
import nl.guidoroos.ticketwave.api.auth.ErrorHandlingInterceptor
import nl.guidoroos.ticketwave.api.auth.TokenStore
import nl.guidoroos.ticketwave.api.repository.*
import nl.guidoroos.ticketwave.domain.repository.*
import org.koin.dsl.module

val apiModule = module {
    single { TokenStore(get()) }
    single { AuthInterceptor(get()) }
    single { ErrorHandlingInterceptor() }
    single<ApolloClient> { ApolloClientProvider.provideApolloClient(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<EventsRepository> { EventsRepositoryImpl(get()) }
}
