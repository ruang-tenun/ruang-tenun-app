package com.ruangtenun.app.data.repository

import com.ruangtenun.app.data.remote.response.LoginResponse
import com.ruangtenun.app.data.remote.response.RegisterResponse
import com.ruangtenun.app.data.remote.api.ApiServiceAuth
import com.ruangtenun.app.data.local.pref.UserModel
import com.ruangtenun.app.data.local.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class AuthRepository private constructor(
    private val userPreference: UserPreference,
    private val apiServiceAuth: ApiServiceAuth
){
    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        return apiServiceAuth.register(username, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiServiceAuth.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiServiceAuth: ApiServiceAuth
        ): AuthRepository = instance ?: synchronized(this) {
            instance ?: AuthRepository(userPreference, apiServiceAuth).also { instance = it }
        }

        fun clearInstance() {
            instance = null
        }
    }
}