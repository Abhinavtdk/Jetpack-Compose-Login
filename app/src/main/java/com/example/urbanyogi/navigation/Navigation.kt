package com.example.urbanyogi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.urbanyogi.composables.HomePage
import com.example.urbanyogi.composables.LoginPage
import com.example.urbanyogi.composables.RegisterPage
import com.example.urbanyogi.composables.TrackPage
import com.example.urbanyogi.model.Tracks
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    var startDestination: String? = Screen.LoginPage.route

    if(firebaseUser!=null){
        startDestination = Screen.HomePage.route + "/{email}"
    }

    NavHost(navController = navController, startDestination = startDestination!!, builder = {
        composable(
            route = Screen.LoginPage.route,
            content = { LoginPage(navController = navController) })
        composable(
            Screen.RegisterPage.route,
            content = { RegisterPage(navController = navController) })
        composable(
//            "home_page/{email}",
            route = Screen.HomePage.route + "/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = "User"
                    nullable = true
                }
            ),
            content = {
                HomePage(
                    navController = navController,
                    email = it.arguments?.getString("email"),
                )
            })

        composable(
            route = Screen.TrackPage.route + "/{track}",
            arguments = listOf(
                navArgument("track") {
                    type = NavType.StringType
                    defaultValue = "Track"
                }
            ),
            content = {backStackEntry ->
                    backStackEntry?.arguments?.getString("track")?.let { json->
                        val track = Gson().fromJson(json,Tracks::class.java)
                        TrackPage(navController = navController, track = track)
                    }
            }
        )

    })
}