package com.ruangtenun.app.data

import com.ruangtenun.app.data.api.response.LoginResponse
import com.ruangtenun.app.data.api.response.RegisterResponse
import com.ruangtenun.app.data.api.retrofit.ApiService
import com.ruangtenun.app.data.pref.UserModel
import com.ruangtenun.app.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
){
    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        return apiService.register(username, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
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
            apiService: ApiService
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, apiService).also { instance = it }
        }

        fun clearInstance() {
            instance = null
        }
    }
}