package com.ruangtenun.app.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtenun.app.data.remote.response.PredictResponse
import com.ruangtenun.app.data.repository.PredictRepository
import com.ruangtenun.app.utils.ResultState
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SearchViewModel(private val predictRepository: PredictRepository) : ViewModel() {

    private val _predictResult = MutableLiveData<ResultState<PredictResponse>>(ResultState.Idle)
    val predictResult: LiveData<ResultState<PredictResponse>> = _predictResult

    fun predict(image: MultipartBody.Part) {
        viewModelScope.launch {
            _predictResult.value = ResultState.Loading

            try {
                val response = predictRepository.predict(image)
                _predictResult.value = ResultState.Success(response)
            } catch (e: Exception) {
                _predictResult.value = ResultState.Error(e.toString())
                Log.d("SearchViewModel", "Error: $e")
            }
        }
    }

    fun resetPredictResult() {
        _predictResult.value = ResultState.Idle
    }
}

