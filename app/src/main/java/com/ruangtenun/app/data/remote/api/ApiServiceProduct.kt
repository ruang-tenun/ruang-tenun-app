package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.remote.response.ListProductResponse
import com.ruangtenun.app.data.remote.response.ProductResponse
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
    @POST("product")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?
    ): Response<AddProductResponse>

    @GET("product")
    suspend fun getAllProduct(
        @Header("Authorization") token: String,
    ): Response<ListProductResponse>

    @GET("product/{id}")
    suspend fun getProductById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<ProductResponse>

}