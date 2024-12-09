package com.ruangtenun.app.di

import android.app.Application
import com.ruangtenun.app.data.local.database.WeavenFabricDatabase
import com.ruangtenun.app.data.remote.api.ApiConfig
import com.ruangtenun.app.data.repository.CatalogRepository
import com.ruangtenun.app.data.repository.ProductRepository
import com.ruangtenun.app.data.repository.HistoryRepository

object Injection {
    fun provideHistoryRepository(application: Application): HistoryRepository {
        val database = WeavenFabricDatabase.getDatabase(application)
        val dao = database.classificationHistoryDao()
        return HistoryRepository.getInstance(dao)
    }

    fun provideProductRepository(): ProductRepository {
        val apiServiceProduct = ApiConfig.getProductService()
        return ProductRepository.getInstance(apiServiceProduct)
    }

    fun provideCatalogRepository(): CatalogRepository {
        val apiServiceCatalog = ApiConfig.getCatalogService()
        return CatalogRepository.getInstance(apiServiceCatalog)
    }
}
