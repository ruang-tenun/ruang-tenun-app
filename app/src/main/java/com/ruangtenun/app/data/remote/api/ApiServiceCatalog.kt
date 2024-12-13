package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.CatalogDetailResponse
import com.ruangtenun.app.data.remote.response.CatalogResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiServiceCatalog {

    @GET("categories")
    suspend fun getAllCatalog(
        @Header("Authorization") token: String,
    ): Response<CatalogResponse>

    @GET("categories/{id}")
    suspend fun getCatalogById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<CatalogDetailResponse>
}