package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.ProductDetailResponse
import com.ruangtenun.app.data.remote.response.ProductsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiServiceProduct {

    @Multipart
    @POST("api/products")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?
    ): Response<ProductsResponse>

    @GET("api/products")
    suspend fun getAllProduct(
        @Header("Authorization") token: String,
    ): Response<ProductsResponse>

    @GET("api/products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ProductDetailResponse>

}