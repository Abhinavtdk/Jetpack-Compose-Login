package com.example.urbanyogi.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.urbanyogi.model.Tracks

@Composable
fun TrackPage(navController: NavController, track: Tracks){

    val trackName = remember {
        mutableStateOf(track.name)
    }

    val trackUrl = remember {
        mutableStateOf(track.trackUrl)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(text = "Playing " + trackName.value, fontSize = 25.sp,modifier = Modifier.padding(16.dp))
//            TrackList(){
//                playTrack(it)
//            }
        }
    }
}