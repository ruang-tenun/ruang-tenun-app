package com.ruangtenun.app.viewmodel.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtenun.app.R
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.data.repository.HistoryRepository
import com.ruangtenun.app.utils.ResultState
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
    private val application: Application
) : ViewModel() {

    private val _allHistories = MediatorLiveData<List<ClassificationHistory>>()
    val allHistories: LiveData<List<ClassificationHistory>> get() = _allHistories

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _saveHistoryState = MutableLiveData<ResultState<String>>()
    val saveHistoryState: LiveData<ResultState<String>> get() = _saveHistoryState

    init {
        fetchAllHistory()
    }

    private fun fetchAllHistory() {
        _loading.postValue(true)
        val source = historyRepository.getAllClassificationHistory()
        _allHistories.addSource(source) { data ->
            _allHistories.value = data
            _loading.postValue(false)

            if (data.isEmpty()) {
                _message.postValue(application.getString(R.string.no_data_found))
            } else {
                _message.postValue("")
            }

            _allHistories.removeSource(source)
        }
    }

    fun savePhotoAndHistory(classificationHistory: ClassificationHistory) {
        viewModelScope.launch {
            val result = historyRepository.insertClassificationHistory(classificationHistory)
            _saveHistoryState.postValue(result)
        }
    }

    fun deleteHistory(classificationHistory: ClassificationHistory) {
        viewModelScope.launch {
            historyRepository.deleteClassificationHistory(classificationHistory)
            fetchAllHistory()
        }
    }
}


