package com.example.checktime

import com.example.checktime.data.db.CheckTimeDao

class CheckTimeRepository(
    private val dao: CheckTimeDao
) {
    suspend fun load(): MutableList<TimeDataHistory> {
        return dao.getAll()
    }

    suspend fun save(item: TimeDataHistory) {
        dao.insert(item)
    }
}