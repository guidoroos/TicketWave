package nl.guidoroos.ticketwave.domain.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun logout(): Result<Boolean>
    suspend fun checkAuth(): Boolean
    suspend fun getToken(): String?
}
