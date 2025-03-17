package com.example.android.pictureinpicture.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TimeRepository {
    fun setTimeMillis(timeMillis: Long)
    fun setStarted(started: Boolean)
    fun getTimeMillis(): StateFlow<Long>
    fun getStarted(): StateFlow<Boolean>
}

class TimerRepositoryImpl private constructor() : TimeRepository {

    // Manually creating Singelton instance. With DI it should be taken care or
    companion object {
        val getInstance: TimerRepositoryImpl by lazy { TimerRepositoryImpl() }
    }

    private val _timeMillis = MutableStateFlow(0L)
    private val _started = MutableStateFlow(false)

    override fun setTimeMillis(timeMillis: Long) {
        _timeMillis.value = timeMillis
    }

    override fun setStarted(started: Boolean) {
        _started.value = started
    }

    override fun getTimeMillis(): StateFlow<Long> = _timeMillis
    override fun getStarted(): StateFlow<Boolean> = _started
}

