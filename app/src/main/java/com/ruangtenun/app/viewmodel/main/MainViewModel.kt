package com.ruangtenun.app.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtenun.app.data.remote.response.AddProductResponse
import com.ruangtenun.app.data.remote.response.CatalogDetail
import com.ruangtenun.app.data.remote.response.CatalogItem
import com.ruangtenun.app.data.remote.response.ProductDetail
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.data.repository.CatalogRepository
import com.ruangtenun.app.data.repository.ProductsRepository
import com.ruangtenun.app.utils.ResultState
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class MainViewModel(
    private val productsRepository: ProductsRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _catalogState = MutableLiveData<ResultState<List<CatalogItem>>>()
    val catalogState: LiveData<ResultState<List<CatalogItem>>> = _catalogState

    private val _catalogDetailState = MutableLiveData<ResultState<CatalogDetail>>()
    val catalogDetailState: LiveData<ResultState<CatalogDetail>> get() = _catalogDetailState

    private val _productState = MutableLiveData<ResultState<List<ProductsItem>>>()
    val productState: LiveData<ResultState<List<ProductsItem>>> get() = _productState

    private val _allProducts = mutableListOf<ProductsItem>()

    private var currentKeyword: String = ""
    private var currentCatalog: String? = null

    private val _productDetailState = MutableLiveData<ResultState<ProductDetail>>()
    val productDetailState: LiveData<ResultState<ProductDetail>> get() = _productDetailState

    init {
        _productState.value = ResultState.Loading
    }

    fun fetchProducts(token: String) {
        _productState.value = ResultState.Loading
        viewModelScope.launch {
            val result = productsRepository.getAllProduct(token)
            if (result is ResultState.Success) {
                _allProducts.clear()
                _allProducts.addAll(result.data)
                filterProducts()
            } else {
                _productState.value = result
            }
        }
    }

    fun fetchProductDetail(token: String, productId: Int) {
        if (_productDetailState.value !is ResultState.Success) {
            _productDetailState.value = ResultState.Loading
            viewModelScope.launch {
                val result = productsRepository.getProductById(productId, token)
                _productDetailState.value = result
            }
        }
    }

    fun fetchCatalogs(token: String) {
        if (_catalogState.value !is ResultState.Success) {
            _catalogState.value = ResultState.Loading
            viewModelScope.launch {
                val result = catalogRepository.getAllCatalog(token)
                _catalogState.value = result
            }
        }
    }

    fun fetchCatalogDetail(token: String, catalogId: Int) {
        if (_catalogDetailState.value !is ResultState.Success) {
            _catalogDetailState.value = ResultState.Loading
            viewModelScope.launch {
                val result = catalogRepository.getCatalogById(catalogId, token)
                _catalogDetailState.value = result
            }
        }
    }

    fun searchProducts(keyword: String) {
        currentKeyword = keyword.trim()
        filterProducts()
    }

    fun filterByCatalog(catalog: String?) {
        currentCatalog = catalog
        filterProducts()
    }

    private fun filterProducts() {
        val filteredByKeyword = if (currentKeyword.isEmpty()) {
            _allProducts
        } else {
            _allProducts.filter { it.name?.contains(currentKeyword, ignoreCase = true) ?: false }
        }

        val filteredByCatalog = if (currentCatalog.isNullOrEmpty() || currentCatalog == "All") {
            filteredByKeyword
        } else {
            filteredByKeyword.filter { it.category == currentCatalog }
        }

        _productState.value = ResultState.Success(filteredByCatalog)
    }


    fun addProduct(
        token: String,
        image: MultipartBody.Part,
        name: String,
        ecommerceUrl: String,
        lat: Double,
        lon: Double
    ): LiveData<ResultState<Response<AddProductResponse>>> {
        return productsRepository.addProduct(token, image, name, ecommerceUrl, lat, lon)
    }
}
