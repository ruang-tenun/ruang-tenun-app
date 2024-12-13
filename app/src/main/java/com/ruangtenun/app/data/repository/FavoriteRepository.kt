package com.ruangtenun.app.data.repository

import com.google.gson.Gson
import com.ruangtenun.app.data.local.pref.UserPreference
import com.ruangtenun.app.data.remote.api.ApiServiceAuth
import com.ruangtenun.app.data.remote.api.ApiServiceFavorite
import com.ruangtenun.app.data.remote.response.FavoriteItem
import com.ruangtenun.app.data.remote.response.FavoriteResponse
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.data.remote.response.ProductsResponse
import com.ruangtenun.app.utils.ResultState
import retrofit2.HttpException
import com.ruangtenun.app.data.repository.AuthRepository

class FavoriteRepository(private val apiServiceFavorite: ApiServiceFavorite) {

    suspend fun getAllFavorite(token: String) : FavoriteResponse {
        return apiServiceFavorite.getAllFavorite(token)
    }

    suspend fun getFavoriteByUserId(token: String, userId: Int): ResultState<List<FavoriteItem>> {
        return try {
            val response = apiServiceFavorite.getFavoriteByUserId("Bearer $token", userId)
            if (response.isSuccessful) {
                ResultState.Success(response.body()?.payload.orEmpty())
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FavoriteResponse::class.java)
                ResultState.Error(errorResponse.message)
            }
        } catch (e: HttpException) {
            ResultState.Error(e.message ?: "HTTP Exception")
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown Exception")
        }
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