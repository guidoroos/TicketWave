package nl.guidoroos.ticketwave.api.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenStore(private val context: Context) {
    private object PreferencesKeys {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.TOKEN_KEY]
        }

    fun getToken(): String? {
        return runBlocking {
            tokenFlow.first()
        }
    }

    suspend fun setToken(token: String?) {
        context.dataStore.edit { preferences ->
            if (token == null) {
                preferences.remove(PreferencesKeys.TOKEN_KEY)
            } else {
                preferences[PreferencesKeys.TOKEN_KEY] = token
            }
        }
    }
}
