package com.ruangtenun.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.ruangtenun.app.data.remote.api.ApiServiceProduct
import com.ruangtenun.app.data.remote.response.AddProductsResponse
import com.ruangtenun.app.data.remote.response.ProductDetail
import com.ruangtenun.app.data.remote.response.ProductDetailResponse
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.data.remote.response.ProductsResponse
import com.ruangtenun.app.utils.ResultState
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.Response

class ProductsRepository(
    private val apiServiceProduct: ApiServiceProduct,
) {

    fun addProduct(
        token: String,
        image: MultipartBody.Part,
        name: String,
        address: String,
        lat: String,
        lon: String,
        categoryId: Int,
        sellerId: Int,
        ecommerceName: String,
        ecommerceLink: String
    ): LiveData<ResultState<Response<AddProductsResponse>>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiServiceProduct.addProduct(
                "Bearer $token", image, name, address, lat, lon, categoryId, sellerId, ecommerceName, ecommerceLink
            )

            if (successResponse.isSuccessful) {
                emit(ResultState.Success(successResponse))
                Log.d("ProductsRepository", "Product added successfully: $successResponse")
            } else {
                val errorBody = successResponse.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddProductsResponse::class.java)
                emit(ResultState.Error(errorResponse.message ?: "Failed with status: ${successResponse.code()}"))
                Log.e("ProductsRepository", "Error response: ${successResponse.code()} - $errorBody")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddProductsResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
            Log.e("ProductsRepository", "HttpException: ${e.message}")
        }

    }

    suspend fun getAllProduct(token: String): ResultState<List<ProductsItem>> {
        return try {
            val response = apiServiceProduct.getAllProduct("Bearer $token")
            if (response.isSuccessful) {
                ResultState.Success(response.body()?.productsItem.orEmpty())
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

    suspend fun getProductById(
        id: Int,
        token: String,
    ): ResultState<ProductDetail> {
        return try {
            val response = apiServiceProduct.getProductById(id, "Bearer $token")
            if (response.isSuccessful) {
                val productList = response.body()?.detailProduct
                val detailProduct = productList?.firstOrNull()
                if (detailProduct != null) {
                    ResultState.Success(detailProduct)
                } else {
                    ResultState.Error("Product not found")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ProductDetailResponse::class.java)
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
        private var instance: ProductsRepository? = null
        fun getInstance(
            apiServiceProduct: ApiServiceProduct,
        ): ProductsRepository =
            instance ?: synchronized(this) {
                instance ?: ProductsRepository(apiServiceProduct)
            }
                .also { instance = it }
    }

}