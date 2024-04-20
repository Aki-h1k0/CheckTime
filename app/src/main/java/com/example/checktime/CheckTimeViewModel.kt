package com.example.checktime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.launch

class CheckTimeViewModel(
    private val repository: CheckTimeRepository
) : ViewModel() {
    private val _time = MutableLiveData<Int>()
    val time: LiveData<Int> = _time
    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String> = _startTime
    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String> = _endTime
    private val _historyList = MutableLiveData<MutableList<TimeDataHistory>>()
    val historyList: LiveData<MutableList<TimeDataHistory>> = _historyList

    fun setTime() {
        val localTimeData = LocalTimeDataProvider.getTimeData()
        _time.value = localTimeData.time
        _startTime.value = localTimeData.startTime
        _endTime.value = localTimeData.endTime
    }

    fun isTimeOn(): Boolean {
        val start = startTime.value!!.substring(0, 2).toInt()
        val end = endTime.value!!.substring(0, 2).toInt()

        val inRange = time.value!! in start..<end
        val inRangeSpanTheDays =
            end < start && (time.value!! in start..23 || time.value!! in 0..<end)
        return inRange || inRangeSpanTheDays
    }

    fun recordHistory(resultMsg: String) {
        val timeDataHistory = TimeDataHistory(
            id = 0,
            time = time.value!!,
            startTime = startTime.value!!,
            endTime = endTime.value!!,
            resultMsg = resultMsg
        )

        val currentList = _historyList.value ?: emptyList()
        val addedList = currentList.toMutableList()
        addedList.add(timeDataHistory)
        _historyList.value = addedList

        viewModelScope.launch {
            repository.save(timeDataHistory)
        }
    }

    fun updateHistory() {
        viewModelScope.launch {
            _historyList.value = repository.load()
        }
    }
}

data class TimeData(
    val time: Int,
    val startTime: String,
    val endTime: String
)

@Entity(tableName = "history")
data class TimeDataHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val time: Int,
    val startTime: String,
    val endTime: String,
    val resultMsg: String
)