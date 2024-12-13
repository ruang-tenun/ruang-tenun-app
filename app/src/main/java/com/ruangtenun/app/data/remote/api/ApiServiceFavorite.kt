package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.FavoriteResponse
import com.ruangtenun.app.data.remote.response.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServiceFavorite {

    @GET("favorite")
    suspend fun getAllFavorite(
        @Header("Authorization") token: String
    ): FavoriteResponse

    @GET("favorite")
    suspend fun getFavoriteByUserId(
        @Header("Authorization") token: String,
        @Query("user") userId: Int
    ): Response<FavoriteResponse>
}