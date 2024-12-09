package com.ruangtenun.app.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ruangtenun.app.data.local.database.ClassificationHistoryDao
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.utils.ResultState
import java.io.File
import java.io.FileOutputStream

class HistoryRepository(
    private val historyDao: ClassificationHistoryDao,
) {

    suspend fun insertClassificationHistory(classificationHistory: ClassificationHistory) {
        historyDao.insertClassificationHistory(classificationHistory)
    }

    suspend fun deleteClassificationHistory(classification: ClassificationHistory) {
        historyDao.deleteClassificationHistory(classification)
    }

    fun getAllClassificationHistory(): LiveData<ResultState<List<ClassificationHistory>>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val data = historyDao.getAllClassificationHistory()
                if (data.isEmpty()) {
                    emit(ResultState.Error("No data found"))
                    Log.d("HistoryRepository", "No data found")
                } else {
                    emit(ResultState.Success(data))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "Unknown Error"))
            }
        }

    fun getClassificationHistoryById(classificationId: Int): LiveData<ClassificationHistory> {
        return historyDao.getClassificationHistoryById(classificationId)
    }

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null
        fun getInstance(
            historyDao: ClassificationHistoryDao
        ): HistoryRepository =
            instance ?: synchronized(this) {
                instance ?: HistoryRepository(historyDao)
            }
                .also { instance = it }
    }
}