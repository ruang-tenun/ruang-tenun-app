package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.AddProductsResponse
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
    @POST("products")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Part image_url: MultipartBody.Part,
        @Part("name") name: String,
        @Part("address") address: String,
        @Part("latitude") latitude: String,
        @Part("longitude") longitude: String,
        @Part("category_id") categoryId: Int,
        @Part("seller_id") sellerId: Int,
        @Part("ecommerce_name") ecommerceName: String,
        @Part("ecommerce_link") ecommerceLink: String
    ): Response<AddProductsResponse>

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