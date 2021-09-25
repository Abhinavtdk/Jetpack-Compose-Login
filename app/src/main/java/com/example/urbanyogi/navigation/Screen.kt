package com.example.urbanyogi.navigation

sealed class Screen(val route: String){
    object LoginPage : Screen("login_page")
    object RegisterPage: Screen("register_page")
    object HomePage: Screen("home_page")
    object TrackPage: Screen("track_page")
}
