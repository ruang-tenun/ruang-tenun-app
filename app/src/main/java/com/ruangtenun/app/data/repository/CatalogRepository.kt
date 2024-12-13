package com.ruangtenun.app.data.repository

import com.google.gson.Gson
import com.ruangtenun.app.data.remote.api.ApiServiceCatalog
import com.ruangtenun.app.data.remote.response.CatalogDetail
import com.ruangtenun.app.data.remote.response.CatalogDetailResponse
import com.ruangtenun.app.data.remote.response.CatalogItem
import com.ruangtenun.app.data.remote.response.ProductsResponse
import com.ruangtenun.app.utils.ResultState
import retrofit2.HttpException

class CatalogRepository(
    private val apiServiceCatalog: ApiServiceCatalog,
) {
    suspend fun getAllCatalog(token: String): ResultState<List<CatalogItem>> {
        return try {
            val response = apiServiceCatalog.getAllCatalog("Bearer $token")
            if (response.isSuccessful) {
                ResultState.Success(response.body()?.catalogItem.orEmpty())
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ProductsResponse::class.java)
                ResultState.Error(errorResponse.message ?: "Unknown error")
            }
        } catch (e: HttpException) {
            ResultState.Error(e.message ?: "HTTP Exception")
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown Exception")
        }
    }

    suspend fun getCatalogById(
        id: Int,
        token: String,
    ): ResultState<CatalogDetail> {
        return try {
            val response = apiServiceCatalog.getCatalogById(id, "Bearer $token")
            if (response.isSuccessful) {
                val catalogDetail = response.body()?.catalogDetail
                if (catalogDetail != null) {
                    ResultState.Success(catalogDetail)
                } else {
                    ResultState.Error("Catalog not found")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, CatalogDetailResponse::class.java)
                ResultState.Error(errorResponse.message ?: "Unknown error")
            }
        } catch (e: HttpException) {
            ResultState.Error(e.message ?: "HTTP Exception")
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown Exception")
        }
    }

    companion object {
        @Volatile
        private var instance: CatalogRepository? = null
        fun getInstance(
            apiServiceCatalog: ApiServiceCatalog,
        ): CatalogRepository =
            instance ?: synchronized(this) {
                instance ?: CatalogRepository(apiServiceCatalog)
            }
                .also { instance = it }
    }
}