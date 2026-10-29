package nl.guidoroos.ticketwave.ui.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import nl.guidoroos.ticketwave.ui.RootViewModel
import nl.guidoroos.ticketwave.ui.screens.basket.BasketViewModel
import nl.guidoroos.ticketwave.ui.screens.checkout.CheckoutViewModel
import nl.guidoroos.ticketwave.ui.screens.eventdetail.EventDetailViewModel
import nl.guidoroos.ticketwave.ui.screens.home.HomeViewModel
import nl.guidoroos.ticketwave.ui.screens.login.LoginViewModel
import nl.guidoroos.ticketwave.ui.screens.setting.SettingViewModel
import nl.guidoroos.ticketwave.ui.screens.tickets.TicketsViewModel

val uiModule = module {
    singleOf(::RootViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::TicketsViewModel)
    viewModelOf(::BasketViewModel)
    viewModelOf(::CheckoutViewModel)
    viewModelOf(::EventDetailViewModel)
    viewModelOf(::SettingViewModel)
}
