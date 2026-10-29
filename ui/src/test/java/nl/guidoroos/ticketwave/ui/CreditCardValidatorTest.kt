package nl.guidoroos.ticketwave.ui

import nl.guidoroos.ticketwave.ui.utils.isValidCreditCard
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CreditCardValidatorTest {

    @Test
    fun testValidCreditCard() {
        assertTrue(isValidCreditCard("4242424242424242"))
    }

    @Test
    fun testInvalidCreditCard() {
        assertFalse(isValidCreditCard("4242424242424243"))
        assertFalse(isValidCreditCard("123"))
        assertFalse(isValidCreditCard(""))
    }
}
