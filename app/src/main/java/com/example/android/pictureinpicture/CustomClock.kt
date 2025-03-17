package com.example.android.pictureinpicture

import android.os.SystemClock


interface FakeClock {
    fun uptimeMillis(): Long
}

class CustomClock : FakeClock {
    override fun uptimeMillis(): Long {
        return SystemClock.uptimeMillis()
    }
}

// For testing only
class TestClock : FakeClock {

    private var currentTimeMillis: Long = 0

    fun setTime(timeMillis: Long) {
        currentTimeMillis = timeMillis
    }

    override fun uptimeMillis(): Long {
        return currentTimeMillis
    }
}

