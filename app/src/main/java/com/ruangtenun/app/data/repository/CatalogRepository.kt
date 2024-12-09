package com.ruangtenun.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.ruangtenun.app.data.remote.api.ApiServiceCatalog
import com.ruangtenun.app.data.remote.api.ApiServiceProduct
import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.remote.response.CatalogResponse
import com.ruangtenun.app.data.remote.response.ListCatalogResponse
import com.ruangtenun.app.data.remote.response.ProductResponse
import com.ruangtenun.app.utils.ResultState
import retrofit2.HttpException
import retrofit2.Response

class CatalogRepository(
    private val apiServiceCatalog: ApiServiceCatalog,
) {
    fun getAllCatalog(
        token: String,
    ): LiveData<ResultState<Response<ListCatalogResponse>>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiServiceCatalog.getAllCatalog(token)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ListCatalogResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
        }
    }

    fun getProductById(
        id: String,
        token: String,
    ): LiveData<ResultState<Response<CatalogResponse>>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiServiceCatalog.getCatalogById(id, token)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, CatalogResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
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