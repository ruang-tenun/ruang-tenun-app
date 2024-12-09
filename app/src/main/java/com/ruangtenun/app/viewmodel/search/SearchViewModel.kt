package com.ruangtenun.app.viewmodel.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtenun.app.data.remote.response.PredictResponse
import com.ruangtenun.app.data.repository.PredictRepository
import com.ruangtenun.app.utils.ResultState
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SearchViewModel(private val predictRepository: PredictRepository) : ViewModel() {

    val predictResult = MutableLiveData<ResultState<PredictResponse>>()

    fun predict(image: MultipartBody.Part) {
        viewModelScope.launch {
            predictResult.value = ResultState.Loading

            try {
                val response = predictRepository.predict(image)
                predictResult.value = ResultState.Success(response)
            } catch (e: Exception) {
                predictResult.value = ResultState.Error(e.toString())
                Log.d("SearchViewModel", "Error: $e")
            }
        }
    }
}