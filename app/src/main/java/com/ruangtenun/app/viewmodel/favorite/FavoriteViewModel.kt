package com.ruangtenun.app.viewmodel.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtenun.app.data.remote.response.FavoriteResponse
import com.ruangtenun.app.data.repository.FavoriteRepository
import com.ruangtenun.app.utils.ResultState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    private val _allFavorite = MutableLiveData<ResultState<FavoriteResponse>>()
    val allFavorite: LiveData<ResultState<FavoriteResponse>> get() = _allFavorite

    private val _favoriteByUserId = MutableLiveData<ResultState<FavoriteResponse>>()
    val favoriteByUserId: LiveData<ResultState<FavoriteResponse>> get() = _favoriteByUserId

    fun getAllFavorite(token: String) {
        viewModelScope.launch {
            _allFavorite.value = ResultState.Loading
             try {
                 val response = favoriteRepository.getAllFavorite(token)
                 _allFavorite.value = ResultState.Success(response)
             } catch (e: HttpException) {
                 _allFavorite.value = ResultState.Error(e.message.toString())
             }
        }
    }

    fun getFavoriteByUserId(token: String, userId: Int) {
        viewModelScope.launch {
            _favoriteByUserId.value = ResultState.Loading

            try {
                val response = favoriteRepository.getFavoriteByUserId("Bearer $token", userId)
                _favoriteByUserId.value = ResultState.Success(response)
            } catch (e: HttpException) {
                _favoriteByUserId.value = ResultState.Error(e.message.toString())
            }
        }
    }
}