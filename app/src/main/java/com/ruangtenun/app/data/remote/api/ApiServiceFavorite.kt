package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.AddFavoriteResponse
import com.ruangtenun.app.data.remote.response.DeleteFavoriteResponse
import com.ruangtenun.app.data.remote.response.FavoriteResponse
import com.ruangtenun.app.data.remote.response.ProductsResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceFavorite {

    @GET("favorite")
    suspend fun getAllFavorite(
        @Header("Authorization") token: String
    ): FavoriteResponse

    @GET("favorites")
    suspend fun getFavoriteByUserId(
        @Header("Authorization") token: String,
        @Query("user") userId: Int
    ): Response<FavoriteResponse>

    @FormUrlEncoded
    @POST("favorites")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Field("product_id") productId: Int
    ): Response<AddFavoriteResponse>

    @DELETE("favorites/{id}")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("id") favoriteId: Int
    ): Response<DeleteFavoriteResponse>
}