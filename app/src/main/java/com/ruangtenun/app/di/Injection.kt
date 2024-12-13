package com.ruangtenun.app.di

import android.content.Context
import com.ruangtenun.app.data.repository.AuthRepository
import com.ruangtenun.app.data.repository.PredictRepository
import android.app.Application
import com.ruangtenun.app.data.local.database.WeavenFabricDatabase
import com.ruangtenun.app.data.local.pref.UserPreference
import com.ruangtenun.app.data.local.pref.dataStore
import com.ruangtenun.app.data.remote.api.ApiConfig
import com.ruangtenun.app.data.repository.CatalogRepository
import com.ruangtenun.app.data.repository.FavoriteRepository
import com.ruangtenun.app.data.repository.ProductsRepository
import com.ruangtenun.app.data.repository.HistoryRepository

object Injection {
    fun provideHistoryRepository(application: Application): HistoryRepository {
        val database = WeavenFabricDatabase.getDatabase(application)
        val dao = database.classificationHistoryDao()
        return HistoryRepository.getInstance(dao, application)
    }

    fun provideProductRepository(): ProductsRepository {
        val apiServiceProduct = ApiConfig.getProductService()
        return ProductsRepository.getInstance(apiServiceProduct)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getAuthService()
        return AuthRepository.getInstance(pref, apiService)
    }

    fun providePredictRepository(): PredictRepository {
        val apiService = ApiConfig.getPredictService()
        return PredictRepository.getInstance(apiService)
    }

    fun provideCatalogRepository(): CatalogRepository {
        val apiServiceCatalog = ApiConfig.getCatalogService()
        return CatalogRepository.getInstance(apiServiceCatalog)
    }

    fun provideFavoriteRepository(): FavoriteRepository {
        val apiServiceFavorite = ApiConfig.getFavoriteService()
        return FavoriteRepository.getInstance(apiServiceFavorite)
    }

}
