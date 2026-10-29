package nl.guidoroos.ticketwave

import android.app.Application
import nl.guidoroos.ticketwave.api.di.apiModule
import nl.guidoroos.ticketwave.domain.di.domainModule
import nl.guidoroos.ticketwave.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TicketWaveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TicketWaveApplication)
            modules(domainModule, apiModule, uiModule)
        }
    }
}
