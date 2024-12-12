package com.ruangtenun.app.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ruangtenun.app.R
import com.ruangtenun.app.data.local.database.ClassificationHistoryDao
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.utils.ResultState

class HistoryRepository(
    private val historyDao: ClassificationHistoryDao,
    private val application: Application
) {
    suspend fun insertClassificationHistory(classificationHistory: ClassificationHistory): ResultState<String> {
        return try {
            historyDao.insertClassificationHistory(classificationHistory)
            ResultState.Success(application.getString(R.string.data_saved_successfully))
        } catch (e: Exception) {
            e.printStackTrace()
            ResultState.Error(application.getString(R.string.failed_to_save_data))
        }
    }

    suspend fun deleteClassificationHistory(classification: ClassificationHistory): ResultState<String> {
        return try {
            historyDao.deleteClassificationHistory(classification)
            ResultState.Success(application.getString(R.string.data_deleted_successfully))
        } catch (e: Exception) {
            e.printStackTrace()
            ResultState.Error(application.getString(R.string.failed_to_delete_data))
        }
    }

    fun getAllClassificationHistory(): LiveData<List<ClassificationHistory>> = liveData {
        try {
            val data = historyDao.getAllClassificationHistory()
            emit(data)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null
        fun getInstance(
            historyDao: ClassificationHistoryDao,
            application: Application
        ): HistoryRepository =
            instance ?: synchronized(this) {
                instance ?: HistoryRepository(historyDao, application)
            }
                .also { instance = it }
    }
}