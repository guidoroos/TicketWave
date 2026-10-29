package nl.guidoroos.ticketwave.domain.model

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val creditCardNumber: String?
)
