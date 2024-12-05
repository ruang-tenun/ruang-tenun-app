package com.ruangtenun.app.di

import android.content.Context
import com.ruangtenun.app.data.local.database.WeavenFabricDatabase
import com.ruangtenun.app.data.repository.WeavenRepository

object Injection {
    fun provideRepository(context: Context): WeavenRepository {
        val database = WeavenFabricDatabase.getDatabase(context)
        val dao = database.classificationHistoryDao()
        return WeavenRepository.getInstance(dao)
    }
}