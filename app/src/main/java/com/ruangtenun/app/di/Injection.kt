package com.ruangtenun.app.di

import android.content.Context
import com.ruangtenun.app.data.repository.UserRepository
import com.ruangtenun.app.data.api.retrofit.ApiConfig
import com.ruangtenun.app.data.pref.UserPreference
import com.ruangtenun.app.data.pref.dataStore
import com.ruangtenun.app.data.repository.PredictRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getAuthService()
        return UserRepository.getInstance(pref, apiService)
    }

    fun providePredictRepository(): PredictRepository {
        val apiService = ApiConfig.getPredictService()
        return PredictRepository.getInstance(apiService)
    }
}