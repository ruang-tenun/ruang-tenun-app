package com.ruangtenun.app.utils

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ruangtenun.app.data.repository.AuthRepository
import com.ruangtenun.app.data.repository.CatalogRepository
import com.ruangtenun.app.data.repository.HistoryRepository
import com.ruangtenun.app.data.repository.PredictRepository
import com.ruangtenun.app.data.repository.ProductRepository
import com.ruangtenun.app.di.Injection
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import com.ruangtenun.app.viewmodel.history.HistoryViewModel
import com.ruangtenun.app.viewmodel.main.MainViewModel
import com.ruangtenun.app.viewmodel.product.ProductViewModel
import com.ruangtenun.app.viewmodel.search.SearchViewModel

class ViewModelFactory private constructor(
    private val productRepository: ProductRepository,
    private val historyRepository: HistoryRepository,
    private val catalogRepository: CatalogRepository,
    private val authRepository: AuthRepository,
    private val predictRepository: PredictRepository
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

            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(predictRepository) as T
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
                val authRepository = Injection.provideAuthRepository(application)
                val predictRepository = Injection.providePredictRepository()

                instance ?: ViewModelFactory(
                    productRepository,
                    historyRepository,
                    catalogRepository,
                    authRepository,
                    predictRepository
                ).also { instance = it }
            }

        @JvmStatic
        fun clearInstance() {
            instance = null
        }
    }
}
