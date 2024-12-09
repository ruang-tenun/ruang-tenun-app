package com.ruangtenun.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.ruangtenun.app.data.remote.api.ApiServiceProduct
import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.remote.response.ListProductResponse
import com.ruangtenun.app.data.remote.response.ProductResponse
import com.ruangtenun.app.utils.ResultState
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class ProductRepository(
    private val apiServiceProduct: ApiServiceProduct,
) {
    fun addProduct(
        token: String,
        imageFile: File,
        lat: Double?,
        lon: Double?
    ): LiveData<ResultState<Response<AddProductResponse>>> = liveData {
        emit(ResultState.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse =
                apiServiceProduct.addProduct(token, multipartBody, lat, lon)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddProductResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
        }
    }

    fun getAllProduct(
        token: String,
    ): LiveData<ResultState<Response<ListProductResponse>>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiServiceProduct.getAllProduct(token)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ListProductResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
        }
    }

    fun getProductById(
        id: String,
        token: String,
    ): LiveData<ResultState<Response<ProductResponse>>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiServiceProduct.getProductById(id, token)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ProductResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
        }
    }

    companion object {
        @Volatile
        private var instance: ProductRepository? = null
        fun getInstance(
            apiServiceProduct: ApiServiceProduct,
        ): ProductRepository =
            instance ?: synchronized(this) {
                instance ?: ProductRepository(apiServiceProduct)
            }
                .also { instance = it }
    }

}