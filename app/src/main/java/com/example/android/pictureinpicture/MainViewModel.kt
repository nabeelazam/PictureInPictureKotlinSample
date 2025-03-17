/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.pictureinpicture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.pictureinpicture.repository.TimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// TODO: Repository should be injected via DI instead
class MainViewModel(
    private val repository: TimeRepository,
    private val clock: FakeClock
) : ViewModel() {

    private var job: Job? = null

    private val timeMillis: StateFlow<Long> = repository.getTimeMillis()
    val started: StateFlow<Boolean> = repository.getStarted()

    val time: StateFlow<String> = timeMillis.map { millis -> formatTime(millis) }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        formatTime(timeMillis.value)
    )

     //to check if the timer needs to be started based on started value
    init {
        if (started.value) {
            start()
        }
    }

    /**
     * Starts the stopwatch if it is not yet started, or pauses it if it is already started.
     */
    fun startOrPause() {
        if (started.value) {
            repository.setStarted(false)
            job?.cancel()
        } else {
            repository.setStarted(true)
            start()
        }
    }

    private fun start() {
        job = viewModelScope.launch {
            val startUptimeMillis = clock.uptimeMillis() - timeMillis.value
            while (isActive) {
                repository.setTimeMillis(clock.uptimeMillis() - startUptimeMillis)
                awaitFrame()
            }
        }
    }


    /**
     * Clears the stopwatch to 00:00:00.
     */
    fun clear() {
        repository.setTimeMillis(0L)
    }

    // Moved the time format into method. Could been moved to extension function
    private fun formatTime(millis: Long): String {
        val minutes = millis / 1000 / 60
        val m = minutes.toString().padStart(2, '0')
        val seconds = (millis / 1000) % 60
        val s = seconds.toString().padStart(2, '0')
        val hundredths = (millis % 1000) / 10
        val h = hundredths.toString().padStart(2, '0')
        return "$m:$s:$h"
    }
}






