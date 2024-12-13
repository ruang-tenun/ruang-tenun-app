package com.ruangtenun.app.data.repository

import com.ruangtenun.app.data.local.pref.UserPreference
import com.ruangtenun.app.data.remote.api.ApiServiceAuth
import com.ruangtenun.app.data.remote.api.ApiServiceFavorite
import com.ruangtenun.app.data.remote.response.FavoriteResponse
import com.ruangtenun.app.data.repository.AuthRepository

class FavoriteRepository(private val apiServiceFavorite: ApiServiceFavorite) {

    suspend fun getAllFavorite(token: String) : FavoriteResponse {
        return apiServiceFavorite.getAllFavorite(token)
    }

    suspend fun getFavoriteByUserId(token: String, userId: Int) : FavoriteResponse {
        return apiServiceFavorite.getFavoriteByUserId(token, userId)
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            apiServiceAuth: ApiServiceFavorite
        ): FavoriteRepository = instance ?: synchronized(this) {
            instance ?: FavoriteRepository(apiServiceAuth).also { instance = it }
        }
    }
}