package com.ruangtenun.app.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id.toString()
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[TIMESTAMP_KEY] = System.currentTimeMillis().toString()
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val timestamp = preferences[TIMESTAMP_KEY]?.toLongOrNull() ?: 0L
            val currentTime = System.currentTimeMillis()
            val oneHourInMillis = 60 * 60 * 1000L

            if (currentTime - timestamp > oneHourInMillis) {
                logout()
                return@map UserModel(0, "", "", "", false)
            } else {
                UserModel(
                    preferences[ID_KEY]?.toInt() ?: 0,
                    preferences[NAME_KEY] ?: "",
                    preferences[EMAIL_KEY] ?: "",
                    preferences[TOKEN_KEY] ?: "",
                    preferences[IS_LOGIN_KEY] == true
                )
            }

        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = stringPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val TIMESTAMP_KEY = stringPreferencesKey("timestamp")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}