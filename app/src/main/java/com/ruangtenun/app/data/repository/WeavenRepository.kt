package com.ruangtenun.app.data.repository

import androidx.lifecycle.LiveData
import com.ruangtenun.app.data.local.database.ClassificationHistoryDao
import com.ruangtenun.app.data.model.ClassificationHistory

class WeavenRepository(
    private val historyDao: ClassificationHistoryDao,
) {

    suspend fun insertClassificationHistory(classification: ClassificationHistory): Long {
        return historyDao.insertClassificationHistory(classification)
    }

    suspend fun deleteClassificationHistory(classification: ClassificationHistory) {
        historyDao.deleteClassificationHistory(classification)
    }

    fun getAllClassificationHistory(): LiveData<List<ClassificationHistory>> {
        return historyDao.getAllClassificationHistory()
    }

    fun getClassificationHistoryById(classificationId: Int): LiveData<ClassificationHistory> {
        return historyDao.getClassificationHistoryById(classificationId)
    }

    companion object {
        @Volatile
        private var instance: WeavenRepository? = null
        fun getInstance(
            historyDao: ClassificationHistoryDao
        ): WeavenRepository =
            instance ?: synchronized(this) {
                instance ?: WeavenRepository(historyDao)
            }
                .also { instance = it }
    }
}