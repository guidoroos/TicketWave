package nl.guidoroos.ticketwave.ui.utils

fun isValidCreditCard(cardNumber: String): Boolean {
    val cleanNumber = cardNumber.filter { it.isDigit() }
    if (cleanNumber.length !in 13..19) return false

    var sum = 0
    var alternate = false
    for (i in cleanNumber.length - 1 downTo 0) {
        var n = cleanNumber[i].toString().toInt()
        if (alternate) {
            n *= 2
            if (n > 9) {
                n = (n % 10) + 1
            }
        }
        sum += n
        alternate = !alternate
    }
    return (sum % 10 == 0)
}
