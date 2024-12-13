package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.FavoriteResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServiceFavorite {

    @GET("api/favorites")
    suspend fun getAllFavorite(
        @Header("Authorization") token: String
    ): FavoriteResponse

    @GET("api/favorites")
    suspend fun getFavoriteByUserId(
        @Header("Authorization") token: String,
        @Query("user") userId: Int
    ): FavoriteResponse
}