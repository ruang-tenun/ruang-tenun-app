package com.ruangtenun.app.view.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ruangtenun.app.data.UserRepository
import com.ruangtenun.app.di.Injection

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): AuthViewModelFactory {
            if (INSTANCE == null) {
                synchronized(AuthViewModelFactory::class.java) {
                    INSTANCE = AuthViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as AuthViewModelFactory
        }

        @JvmStatic
        fun clearInstance() {
            INSTANCE = null
        }
    }
}