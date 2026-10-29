package nl.guidoroos.ticketwave.api.auth

import nl.guidoroos.ticketwave.domain.error.NetworkError
import okhttp3.Interceptor
import okhttp3.Response

class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        when (response.code) {
            401 -> throw NetworkError.Unauthorized(401)
            404 -> throw NetworkError.NotFound(404)
            in 500..599 -> throw NetworkError.ServerError(response.code)
            in 400..499 -> throw NetworkError.Unknown(response.code, response.message)
        }
        return response
    }
}
