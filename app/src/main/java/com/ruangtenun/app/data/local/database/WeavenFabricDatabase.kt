package com.ruangtenun.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ruangtenun.app.data.model.ClassificationHistory

@Database(entities = [ClassificationHistory::class], version = 1, exportSchema = false)
abstract class WeavenFabricDatabase : RoomDatabase() {
    abstract fun classificationHistoryDao(): ClassificationHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: WeavenFabricDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): WeavenFabricDatabase {
            if (INSTANCE == null) {
                synchronized(WeavenFabricDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WeavenFabricDatabase::class.java, "ruang_tenun_database"
                    ).build()
                }
            }
            return INSTANCE as WeavenFabricDatabase
        }
    }
}