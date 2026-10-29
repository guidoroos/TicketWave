package nl.guidoroos.ticketwave.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import nl.guidoroos.ticketwave.ui.events.AppEvent
import nl.guidoroos.ticketwave.ui.navigation.Screen
import nl.guidoroos.ticketwave.ui.navigation.TopBarConfig
import nl.guidoroos.ticketwave.ui.screens.barcode.BarcodeScreen
import nl.guidoroos.ticketwave.ui.screens.basket.BasketScreen
import nl.guidoroos.ticketwave.ui.screens.checkout.CheckoutScreen
import nl.guidoroos.ticketwave.ui.screens.eventdetail.EventDetailScreen
import nl.guidoroos.ticketwave.ui.screens.home.HomeScreen
import nl.guidoroos.ticketwave.ui.screens.login.LoginScreen
import nl.guidoroos.ticketwave.ui.screens.setting.SettingScreen
import nl.guidoroos.ticketwave.ui.screens.tickets.TicketsScreen
import nl.guidoroos.ticketwave.ui.theme.TicketWaveTheme
import androidx.navigation.NavDestination.Companion.hasRoute

data class NavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

@Composable
fun Root(
    viewModel: RootViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    TicketWaveTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        var currentDialog by remember { mutableStateOf<AppEvent.ShowDialog?>(null) }

        val topBarComposable = TopBarConfig.getTopBar(
            destination = currentDestination,
            onNavigateToProfile = { navController.navigate(Screen.Setting) },
            onBack = { navController.popBackStack() }
        )

        val onEvent: (AppEvent) -> Unit = { event ->
            when (event) {
                is AppEvent.ShowSnackbar -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                is AppEvent.ShowDialog -> {
                    currentDialog = event
                }
            }
        }

        val bottomNavScreens = listOf(
            NavItem(Screen.Home, "Home", Icons.Default.Home),
            NavItem(Screen.Tickets, "Tickets", Icons.AutoMirrored.Filled.List),
            NavItem(Screen.Basket, "Basket", Icons.Default.ShoppingCart)
        )

        val showBottomBar = bottomNavScreens.any { 
            when (it.screen) {
                is Screen.Home -> currentDestination?.hasRoute<Screen.Home>() == true
                is Screen.Tickets -> currentDestination?.hasRoute<Screen.Tickets>() == true
                is Screen.Basket -> currentDestination?.hasRoute<Screen.Basket>() == true
                else -> false
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = topBarComposable,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        bottomNavScreens.forEach { item ->
                            val isSelected = when (item.screen) {
                                is Screen.Home -> currentDestination?.hasRoute<Screen.Home>() == true
                                is Screen.Tickets -> currentDestination?.hasRoute<Screen.Tickets>() == true
                                is Screen.Basket -> currentDestination?.hasRoute<Screen.Basket>() == true
                                else -> false
                            }
                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = {
                                    Text(
                                        text = item.label,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) {
                                        navController.navigate(item.screen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = false
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = viewModel.startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<Screen.Login> {
                    LoginScreen(
                        onEvent = onEvent,
                        onLoginSuccess = {
                            navController.navigate(Screen.Home) {
                                popUpTo<Screen.Login> { inclusive = true }
                            }
                        }
                    )
                }
                composable<Screen.Home> {
                    HomeScreen(
                        onEvent = onEvent,
                        onEventClick = { event ->
                            navController.navigate(Screen.EventDetail(event.id))
                        }
                    )
                }
                composable<Screen.Tickets> { TicketsScreen(onEvent = onEvent) }
                composable<Screen.Basket> {
                    BasketScreen(
                        onEvent = onEvent,
                        onContinueShopping = {
                            navController.navigate(Screen.Home) {
                                popUpTo<Screen.Home> { inclusive = true }
                            }
                        },
                        onNavigateToCheckout = {
                            navController.navigate(Screen.Checkout)
                        }
                    )
                }
                composable<Screen.Checkout> {
                    CheckoutScreen(
                        onEvent = onEvent,
                        onCheckoutSuccess = {
                            navController.navigate(Screen.Home) {
                                popUpTo<Screen.Home> { inclusive = true }
                            }
                        }
                    )
                }
                composable<Screen.EventDetail> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.EventDetail>()
                    EventDetailScreen(
                        eventId = args.eventId,
                        onEvent = onEvent,
                        onNavigateToBasket = {
                            navController.navigate(Screen.Basket) {
                                popUpTo<Screen.Home> { inclusive = false }
                            }
                        }
                    )
                }
                composable<Screen.Barcode> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.Barcode>()
                    BarcodeScreen(
                        barcodeNumber = args.barcodeNumber,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable<Screen.Setting> {
                    SettingScreen(
                        onEvent = onEvent,
                        onNavigateToTickets = {
                            navController.navigate(Screen.Tickets) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onLogoutSuccess = {
                            navController.navigate(Screen.Login) {
                                popUpTo<Screen.Login> { inclusive = true }
                            }
                        }
                    )
                }
            }

            currentDialog?.let { dialog ->
                AlertDialog(
                    onDismissRequest = { currentDialog = null },
                    title = {
                        Text(
                            text = dialog.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    text = {
                        Text(
                            text = dialog.message,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                dialog.onConfirm()
                                currentDialog = null
                            }
                        ) {
                            Text(
                                text = "OK",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                )
            }
        }
    }
}
