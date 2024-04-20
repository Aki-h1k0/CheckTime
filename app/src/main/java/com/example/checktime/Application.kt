package com.example.checktime

import android.app.Application
import androidx.room.Room
import com.example.checktime.data.db.CheckTimeDatabase

class Application: Application() {
    companion object {
        lateinit var database: CheckTimeDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            CheckTimeDatabase::class.java,
            "check_time_database"
        ).build()
    }
}