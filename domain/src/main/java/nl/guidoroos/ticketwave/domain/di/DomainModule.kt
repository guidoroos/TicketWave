package nl.guidoroos.ticketwave.domain.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {
    single<CoroutineDispatcher>(named("IODispatcher")) { Dispatchers.IO }
}
