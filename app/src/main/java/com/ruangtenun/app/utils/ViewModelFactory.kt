package com.ruangtenun.app.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ruangtenun.app.data.repository.CatalogRepository
import com.ruangtenun.app.data.repository.HistoryRepository
import com.ruangtenun.app.data.repository.ProductRepository
import com.ruangtenun.app.di.Injection
import com.ruangtenun.app.viewmodel.history.HistoryViewModel
import com.ruangtenun.app.viewmodel.main.MainViewModel
import com.ruangtenun.app.viewmodel.product.ProductViewModel

class ViewModelFactory private constructor(
    private val productRepository: ProductRepository,
    private val historyRepository: HistoryRepository,
    private val catalogRepository: CatalogRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(productRepository) as T
            }

            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                ProductViewModel(productRepository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(historyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(application: Application): ViewModelFactory =
            instance ?: synchronized(this) {
                val productRepository = Injection.provideProductRepository()
                val historyRepository = Injection.provideHistoryRepository(application)
                val catalogRepository = Injection.provideCatalogRepository()

                instance ?: ViewModelFactory(
                    productRepository,
                    historyRepository,
                    catalogRepository
                ).also { instance = it }
            }
    }
}
