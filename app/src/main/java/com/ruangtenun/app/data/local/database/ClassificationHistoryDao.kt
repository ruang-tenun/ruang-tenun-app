package com.ruangtenun.app.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruangtenun.app.data.model.ClassificationHistory

@Dao
interface ClassificationHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClassificationHistory(classification: ClassificationHistory): Long

    @Delete
    suspend fun deleteClassificationHistory(classification: ClassificationHistory)

    @Query("SELECT * from classification_history")
    suspend fun getAllClassificationHistory(): List<ClassificationHistory>

    @Query("SELECT * FROM classification_history WHERE classification_id = :classificationId")
    fun getClassificationHistoryById(classificationId: Int): LiveData<ClassificationHistory>
}