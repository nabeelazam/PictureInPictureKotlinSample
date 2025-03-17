package com.example.android.pictureinpicture.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.pictureinpicture.CustomClock
import com.example.android.pictureinpicture.MainViewModel
import com.example.android.pictureinpicture.repository.TimeRepository

class MainViewModelFactory(
    private val repository: TimeRepository,
    private val clock: CustomClock
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository, clock) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}