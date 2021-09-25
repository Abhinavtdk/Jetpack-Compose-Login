package com.example.urbanyogi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urbanyogi.repository.TracksRepository

class TrackViewModelFactory(private val tracksRepository: TracksRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TrackViewModel::class.java)){
            return TrackViewModel(tracksRepository = tracksRepository) as T
        }
        throw IllegalStateException()
    }

}