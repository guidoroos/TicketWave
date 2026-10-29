package nl.guidoroos.ticketwave.domain.model

data class Cart(
    val items: List<CartItem>,
    val creditCardNumber: String?
)
