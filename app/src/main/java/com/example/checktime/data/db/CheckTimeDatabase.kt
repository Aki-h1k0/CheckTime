package com.example.checktime.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.checktime.TimeDataHistory
import com.example.checktime.data.Converters
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [TimeDataHistory::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CheckTimeDatabase : RoomDatabase() {
    abstract fun checkTimeDao(): CheckTimeDao
    companion object {
        @Volatile
        private var INSTANCE: CheckTimeDatabase? = null
        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): CheckTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CheckTimeDatabase::class.java,
                    "check_time_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}