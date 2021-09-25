package com.example.urbanyogi.composables

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navArgument
import coil.compose.rememberImagePainter
import com.example.urbanyogi.TracksResponse
import com.example.urbanyogi.model.Tracks
import com.example.urbanyogi.navigation.Screen
import com.example.urbanyogi.repository.RegisterRepository
import com.example.urbanyogi.repository.TracksRepository
import com.example.urbanyogi.viewmodel.RegisterViewModel
import com.example.urbanyogi.viewmodel.RegisterViewModelFactory
import com.example.urbanyogi.viewmodel.TrackViewModel
import com.example.urbanyogi.viewmodel.TrackViewModelFactory
import com.google.accompanist.coil.CoilImage
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

@Composable
fun HomePage(
    navController: NavController, email: String?,
    registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(RegisterRepository())
    ),
) {

    val name = remember {
        mutableStateOf("")
    }
    name.value = email ?: "No Argument"
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    if (firebaseUser != null) {
        name.value = firebaseUser.email.toString()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {

            Button(
                onClick = {
                    registerViewModel.signOut()
                    navController.navigate(Screen.LoginPage.route)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Sign Out")
            }

            Text(
                text = "Welcome " + name.value,
                fontSize = 25.sp,
                modifier = Modifier.padding(16.dp)
            )
            TrackList() {
                playTrack(it, navController)
            }
        }
    }
}

fun playTrack(track: Tracks, navController: NavController) {
    val trackJson = Gson().toJson(track)
    navController.navigate(Screen.TrackPage.route + "/${trackJson}")
}


@Composable
fun TrackList(
    trackViewModel: TrackViewModel = viewModel(
        factory = TrackViewModelFactory(TracksRepository())
    ),
    selectedItem: (Tracks) -> Unit
) {
    when (val tracksList = trackViewModel.getTrackDetails().collectAsState(initial = null).value) {

        is TracksResponse.onError -> {
            Text(text = "There's some error")
        }

        is TracksResponse.onSuccess -> {
            val listOfTracks = tracksList.querySnapshot?.toObjects(Tracks::class.java)
            listOfTracks?.let {
                Column() {
                    Text(
                        text = "Track List",
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
                        items(listOfTracks) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                TrackDetails(it, selectedItem = selectedItem)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TrackDetails(track: Tracks, selectedItem: (Tracks) -> Unit) {

    var showPlayer by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.clickable {
//        selectedItem(track)
        showPlayer = showPlayer.not()
    }) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberImagePainter(track.image),
                contentDescription = "Track image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(100.dp),
            )

            Column {
                Text(
                    text = "${track.name}",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                )
                Text(
                    text = "${track.artistName}",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
            }


            Image(
                painter = rememberImagePainter(track.artistImage),
                contentDescription = "Artist image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )

        }
        
        AnimatedVisibility(visible = showPlayer) {
            Text(text = track.trackUrl,
            modifier = Modifier.padding(16.dp))
        }
    }

}
