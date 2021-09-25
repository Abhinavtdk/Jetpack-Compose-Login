package com.example.urbanyogi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urbanyogi.TracksResponse
import com.example.urbanyogi.model.Tracks
import com.example.urbanyogi.repository.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TrackViewModel(private val tracksRepository: TracksRepository): ViewModel() {

//    val tracksStateFlow = MutableStateFlow<List<Tracks>>()
//
//    init {
//        viewModelScope.launch {
//            tracksRepository.getTrackDetails().collect{
//                tracksStateFlow.value = it
//            }
//        }
//    }

    fun getTrackDetails() = tracksRepository.getTrackDetails()

}