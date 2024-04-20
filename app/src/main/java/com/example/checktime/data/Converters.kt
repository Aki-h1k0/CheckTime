package com.example.checktime.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListString(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun toListString(data: String): List<String> = listOf(*data.split(",").toTypedArray())
}