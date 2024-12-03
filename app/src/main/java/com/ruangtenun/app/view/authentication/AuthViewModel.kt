package com.ruangtenun.app.view.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ruangtenun.app.data.ResultState
import com.ruangtenun.app.data.UserRepository
import com.ruangtenun.app.data.api.response.LoginResponse
import com.ruangtenun.app.data.api.response.RegisterResponse
import com.ruangtenun.app.data.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    val registerResult = MutableLiveData<ResultState<RegisterResponse>>()
    val loginResult = MutableLiveData<ResultState<LoginResponse>>()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            registerResult.value = ResultState.Loading

            try {
                val response = userRepository.register(name, email, password)
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
                val response = userRepository.login(email, password)
                val name = response.payload.username
                val token = response.token
                val userModel =
                    UserModel(name = name, email = email, token = token, isLogin = true)
                saveSession(userModel)
                loginResult.value = ResultState.Success(response)
            } catch (e: Exception) {
                val errorMessage =
                    (e as? HttpException)?.response()?.errorBody()?.string() ?: e.localizedMessage
                loginResult.value = ResultState.Error(errorMessage)
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

}