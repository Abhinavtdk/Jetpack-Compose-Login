package com.example.urbanyogi.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urbanyogi.model.Tracks
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.lang.IllegalArgumentException

class PlayTrackViewModel(track : Tracks, context: Context): ViewModel() {

    var autoPlay = true
    var window = 0
    var position = 0L

    var player: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
        .build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                context, Util.getUserAgent(context, context.packageName)
            )

            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(track.trackUrl))

            this.prepare(source)
            this.playWhenReady = autoPlay
            this.seekTo(window , position)
        }

    fun updateState(){
        this.autoPlay = player.playWhenReady
        this.position = 0L.coerceAtLeast(player.currentPosition)
        this.window = player.currentWindowIndex
    }

    fun onStart(){
        player.playWhenReady = autoPlay
    }

    fun onStop(){
        updateState()
        player.playWhenReady = false
    }

}

class PlayTrackViewModelFactory(
    private val track: Tracks,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlayTrackViewModel::class.java)){
            return PlayTrackViewModel(track, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}