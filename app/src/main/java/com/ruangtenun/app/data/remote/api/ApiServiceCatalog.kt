package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.remote.response.CatalogResponse
import com.ruangtenun.app.data.remote.response.ListCatalogResponse
import com.ruangtenun.app.data.remote.response.ListProductResponse
import com.ruangtenun.app.data.remote.response.ProductResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiServiceCatalog {

    @GET("catalog")
    suspend fun getAllCatalog(
        @Header("Authorization") token: String,
    ): Response<ListCatalogResponse>

    @GET("catalog/{id}")
    suspend fun getCatalogById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<CatalogResponse>
}