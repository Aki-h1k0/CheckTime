package com.example.checktime

import java.util.Calendar

object LocalTimeDataProvider {
    fun getTimeData() = TimeData(
        time = (0 until 24).random(),
        startTime = setTimeForRange(),
        endTime = setTimeForRange()
    )

    private fun setTimeForRange(): String {
        var time = ""
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, (0 until 24).random())
        calendar.set(Calendar.MINUTE, (0 until 60).random())
        val h = calendar.get(Calendar.HOUR_OF_DAY)
        val hour = if (h < 10) "0$h" else h
        val m = calendar.get(Calendar.MINUTE)
        val minute = if (m < 10) "0$m" else m
        time = "$hour:$minute"
        // APIレベル26未満は使用不可のため、Calendarで対応
//        time = LocalTime.of((0 until 24).random(), (0 until 60).random()).toString()
        return time
    }
}