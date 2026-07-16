package com.ruangtenun.app.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ruangtenun.app.data.repository.AuthRepository
import com.ruangtenun.app.data.remote.response.LoginResponse
import com.ruangtenun.app.data.remote.response.RegisterResponse
import com.ruangtenun.app.data.local.pref.UserModel
import com.ruangtenun.app.utils.ResultState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val registerResult = MutableLiveData<ResultState<RegisterResponse>>()
    val loginResult = MutableLiveData<ResultState<LoginResponse>>()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            registerResult.value = ResultState.Loading

            try {
                val response = authRepository.register(name, email, password)
                registerResult.value = ResultState.Success(response)
            } catch (e: Exception) {
                val errorMessage =
                    (e as? HttpException)?.response()?.errorBody()?.string() ?: e.localizedMessage
                registerResult.value = ResultState.Error(errorMessage)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginResult.value = ResultState.Loading

            try {
                val response = authRepository.login(email, password)
                val id = response.payload.id
                val name = response.payload.username
                val token = response.token
                val userModel =
                    UserModel(id = id, name = name, email = email, token = token, isLogin = true)
                saveSession(userModel)
                loginResult.value = ResultState.Success(response)
                Log.d("LoginViewModel", "Login success: $response")
            } catch (e: Exception) {
                val errorMessage =
                    (e as? HttpException)?.response()?.errorBody()?.string() ?: e.localizedMessage
                loginResult.value = ResultState.Error(errorMessage)
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            authRepository.saveSession(user)
            Log.d("AuthViewModel", "Session saved: $user")
        }
    }

    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}