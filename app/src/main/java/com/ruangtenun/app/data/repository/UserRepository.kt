package com.ruangtenun.app.data.repository

import com.ruangtenun.app.data.api.response.LoginResponse
import com.ruangtenun.app.data.api.response.RegisterResponse
import com.ruangtenun.app.data.api.retrofit.ApiServiceAuth
import com.ruangtenun.app.data.pref.UserModel
import com.ruangtenun.app.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
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
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiServiceAuth: ApiServiceAuth
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, apiServiceAuth).also { instance = it }
        }

        fun clearInstance() {
            instance = null
        }
    }
}