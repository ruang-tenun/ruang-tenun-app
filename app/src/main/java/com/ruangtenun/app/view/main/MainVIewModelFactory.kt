package com.ruangtenun.app.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ruangtenun.app.data.repository.PredictRepository
import com.ruangtenun.app.di.Injection
import com.ruangtenun.app.view.main.search.SearchViewModel

class MainVIewModelFactory(private val predictRepository: PredictRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(predictRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var INSTANCE: MainVIewModelFactory? = null

        @JvmStatic
        fun getInstance(): MainVIewModelFactory {
            if (INSTANCE == null) {
                synchronized(MainVIewModelFactory::class.java) {
                    INSTANCE = MainVIewModelFactory(Injection.providePredictRepository())
                }
            }
            return INSTANCE as MainVIewModelFactory
        }
    }
}