package nl.guidoroos.ticketwave.ui

import nl.guidoroos.ticketwave.ui.utils.formatCurrency
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyFormatterTest {

    @Test
    fun testFormatCurrencyEuro() {
        assertEquals("€36.96", formatCurrency(3696, "EUR"))
    }

    @Test
    fun testFormatCurrencyDollar() {
        assertEquals("$25.37", formatCurrency(2537, "USD"))
    }
}
