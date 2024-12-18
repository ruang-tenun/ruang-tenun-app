package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.remote.response.ProductDetailResponse
import com.ruangtenun.app.data.remote.response.ProductsResponse
import okhttp3.MultipartBody
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
        @Part image: MultipartBody.Part,
        @Part("name") name: String,
        @Part("ecommerce_url") ecommerceUrl: String,
        @Part("latitude") latitude: Double,
        @Part("longitude") longitude: Double
    ): Response<AddProductResponse>

    @GET("products")
    suspend fun getAllProduct(
        @Header("Authorization") token: String,
    ): Response<ProductsResponse>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ProductDetailResponse>

}