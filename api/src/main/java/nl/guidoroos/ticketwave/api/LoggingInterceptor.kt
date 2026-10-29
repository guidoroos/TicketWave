package nl.guidoroos.ticketwave.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

class LoggingInterceptor : Interceptor {
    private val TAG = "TicketWaveNetwork"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val requestBody = request.body?.let { body ->
            try {
                val buffer = Buffer()
                body.writeTo(buffer)
                buffer.readString(Charset.forName("UTF-8"))
            } catch (e: Exception) {
                "[Body read error: ${e.message}]"
            }
        } ?: "(no request body)"

        Log.d(TAG, "==> REQUEST: $url\nBody: $requestBody")

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e(TAG, "<== REQUEST FAILED: $url | Error: ${e.message}", e)
            throw e
        }

        val code = response.code
        val errorMessage = if (!response.isSuccessful) response.message else "(none)"
        val responseBody = try {
            response.peekBody(Long.MAX_VALUE).string()
        } catch (e: Exception) {
            "[Body read error: ${e.message}]"
        }

        Log.d(TAG, "<== RESPONSE [$code] for $url\nError Message: $errorMessage\nBody: $responseBody")

        return response
    }
}
