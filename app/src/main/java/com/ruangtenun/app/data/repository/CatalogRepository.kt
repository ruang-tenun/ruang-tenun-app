package com.ruangtenun.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.ruangtenun.app.data.remote.api.ApiServiceCatalog
import com.ruangtenun.app.data.remote.response.CatalogDetail
import com.ruangtenun.app.data.remote.response.CatalogDetailResponse
import com.ruangtenun.app.data.remote.response.CatalogItem
import com.ruangtenun.app.data.remote.response.CatalogResponse
import com.ruangtenun.app.data.remote.response.ProductDetail
import com.ruangtenun.app.data.remote.response.ProductDetailResponse
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.data.remote.response.ProductsResponse
import com.ruangtenun.app.utils.ResultState
import retrofit2.HttpException
import retrofit2.Response

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
                val catalogList = response.body()?.catalogDetail
                val detailCatalog = catalogList?.firstOrNull()
                if (detailCatalog != null) {
                    ResultState.Success(detailCatalog)
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