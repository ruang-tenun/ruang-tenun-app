package com.ruangtenun.app.viewmodel.product

import androidx.lifecycle.LiveData
import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.repository.ProductRepository
import com.ruangtenun.app.utils.ResultState
import retrofit2.Response
import java.io.File

class ProductViewModel(
    private val productRepository: ProductRepository
) {
    fun addProduct(
        token: String,
        file: File,
        lat: Double?,
        lon: Double?
    ): LiveData<ResultState<Response<AddProductResponse>>> {
        return productRepository.addProduct(token, file, lat, lon)
    }

    fun getAllProduct(
        token: String
    ) = productRepository.getAllProduct(token)

    fun getProductById(
        id: String,
        token: String
    ) = productRepository.getProductById(id, token)

}