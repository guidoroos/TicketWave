package nl.guidoroos.ticketwave.domain.error

sealed class NetworkError(message: String) : Exception(message) {
    class NoInternet : NetworkError("No internet connection available. Please check your network.")
    data class Unauthorized(val code: Int = 401) : NetworkError("Unauthorized. Please check your credentials.")
    data class NotFound(val code: Int = 404) : NetworkError("Resource not found.")
    data class ServerError(val code: Int = 500) : NetworkError("Server error. Please try again later.")
    data class Unknown(val code: Int, val errMessage: String) : NetworkError("Network error ($code): $errMessage")
    data class Generic(override val message: String) : NetworkError(message)
}
