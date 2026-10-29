package nl.guidoroos.ticketwave.ui.utils

import java.util.Locale

fun formatCurrency(priceCents: Int, currency: String): String {
    val currencySymbol = when (currency.uppercase()) {
        "EUR", "€" -> "€"
        "USD", "$" -> "$"
        else -> currency
    }
    val formattedPrice = String.format(Locale.ROOT, "%.2f", priceCents / 100.0)
    return "$currencySymbol$formattedPrice"
}
