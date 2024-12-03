package com.ruangtenun.app.di

import android.content.Context
import com.ruangtenun.app.data.UserRepository
import com.ruangtenun.app.data.api.retrofit.ApiConfig
import com.ruangtenun.app.data.pref.UserPreference
import com.ruangtenun.app.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}