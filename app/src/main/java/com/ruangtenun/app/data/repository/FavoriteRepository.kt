package com.ruangtenun.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.ruangtenun.app.data.remote.api.ApiServiceFavorite
import com.ruangtenun.app.data.remote.response.AddFavoriteResponse
import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.remote.response.DeleteFavoriteResponse
import com.ruangtenun.app.data.remote.response.FavoriteItem
import com.ruangtenun.app.data.remote.response.FavoriteResponse
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.data.remote.response.ProductsResponse
import com.ruangtenun.app.utils.ResultState
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.Response

class FavoriteRepository(private val apiServiceFavorite: ApiServiceFavorite) {

    suspend fun getAllFavorite(token: String): FavoriteResponse {
        return apiServiceFavorite.getAllFavorite(token)
    }

    suspend fun getFavoriteByUserId(token: String, userId: Int): ResultState<List<FavoriteItem>> {
        return try {
            val response = apiServiceFavorite.getFavoriteByUserId("Bearer $token", userId)
            if (response.isSuccessful) {
                Log.d("FavoriteRepository", "Error response: $response")
                ResultState.Success(response.body()?.payload.orEmpty())
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FavoriteResponse::class.java)
                Log.d("FavoriteRepository", "Error response: $errorResponse")
                ResultState.Error(errorResponse.message)
            }
        } catch (e: HttpException) {
            ResultState.Error(e.message ?: "HTTP Exception")
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown Exception")
        }
    }

    suspend fun addFavorite(
        token: String,
        userId: Int,
        productId: Int
    ): ResultState<AddFavoriteResponse> {
        return try {
            val response = apiServiceFavorite.addFavorite(token, userId, productId)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    ResultState.Success(body)
                } ?: ResultState.Error("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddFavoriteResponse::class.java)
                ResultState.Error(errorResponse.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun removeFavorite(
        token: String,
        favoriteId: Int
    ): ResultState<DeleteFavoriteResponse> {
        return try {
            val response = apiServiceFavorite.removeFavorite(token, favoriteId)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    ResultState.Success(body)
                } ?: ResultState.Error("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, DeleteFavoriteResponse::class.java)
                ResultState.Error(errorResponse.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
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