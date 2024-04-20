package com.example.checktime.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.checktime.TimeDataHistory

@Dao
interface CheckTimeDao {
    @Query("SELECT * FROM history")
    suspend fun getAll(): MutableList<TimeDataHistory>

    @Insert
    suspend fun insert(historyList: TimeDataHistory)
}