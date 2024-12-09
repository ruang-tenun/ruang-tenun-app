package com.ruangtenun.app.viewmodel.history

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.data.repository.HistoryRepository
import com.ruangtenun.app.utils.ResultState
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _allHistoryState = MutableLiveData<ResultState<List<ClassificationHistory>>>()
    val allHistoryState: LiveData<ResultState<List<ClassificationHistory>>> get() = _allHistoryState

    init {
        fetchAllHistory()
    }

    private fun fetchAllHistory() {
        viewModelScope.launch {
            historyRepository.getAllClassificationHistory().observeForever { state ->
                _allHistoryState.postValue(state)
            }
        }
    }

    fun savePhotoAndHistory(classificationHistory: ClassificationHistory) {
        viewModelScope.launch {
            historyRepository.insertClassificationHistory(classificationHistory)
            fetchAllHistory()
        }
    }
}


