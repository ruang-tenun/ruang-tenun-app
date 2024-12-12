package com.ruangtenun.app.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.repository.ProductRepository
import com.ruangtenun.app.utils.ResultState
import okhttp3.MultipartBody
import retrofit2.Response

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    fun addProduct(
        token: String,
        image: MultipartBody.Part,
        name: String,
        ecommerceUrl: String,
        lat: Double,
        lon: Double
    ): LiveData<ResultState<Response<AddProductResponse>>> {
        return productRepository.addProduct(token, image, name, ecommerceUrl, lat, lon)
    }

    fun getAllProduct(
        token: String
    ) = productRepository.getAllProduct(token)

    fun getProductById(
        id: String,
        token: String
    ) = productRepository.getProductById(id, token)

}