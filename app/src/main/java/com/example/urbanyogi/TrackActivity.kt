package com.example.urbanyogi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.urbanyogi.model.Tracks
import com.example.urbanyogi.ui.theme.UrbanYogiTheme
import com.example.urbanyogi.ui.theme.primaryColor
import com.example.urbanyogi.ui.theme.whiteBackground
import com.example.urbanyogi.viewmodel.PlayTrackViewModel
import com.example.urbanyogi.viewmodel.PlayTrackViewModelFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util

class TrackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UrbanYogiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val track: Tracks by lazy {
                        intent?.getSerializableExtra(TRACK_ID) as Tracks
                    }
                    PlayTrack(track)
                }
            }
        }
    }

    companion object {
        private const val TRACK_ID = "track_name"
        fun newIntent(context: Context, track: Tracks) =
            Intent(context, TrackActivity::class.java).apply {
                putExtra(TRACK_ID, track)
            }
    }
}

@Composable
fun PlayTrack(track: Tracks) {

    Text(text = "Hello ${track.name}!")
    Player(track)
}

@Composable
fun Player(
    track: Tracks,
    playTrackViewModel: PlayTrackViewModel = viewModel(
        factory = PlayTrackViewModelFactory(track, LocalContext.current)
    )
) {
    val context = LocalContext.current

    var autoPlay = remember {
        mutableStateOf(true)
    }
    var window = remember {
        mutableStateOf(0)
    }
    var position = remember {
        mutableStateOf(0L)
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .background(whiteBackground),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Playing ${track.name}",
                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.padding(16.dp)
            )

            Image(
                painter = rememberImagePainter(data = track.image),
                contentDescription = "Track Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .padding(24.dp)
            )
        }

        val exoPlayer = playTrackViewModel.player

        DisposableEffect(key1 = playTrackViewModel) {
            playTrackViewModel.onStart()
            onDispose {
                playTrackViewModel.onStop()
            }
        }

        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

