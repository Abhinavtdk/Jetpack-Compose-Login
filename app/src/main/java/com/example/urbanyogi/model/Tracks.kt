package com.example.urbanyogi.model

import java.io.Serializable

data class Tracks(
//    val id: String,
    val name: String,
    val image: String,
    val artistName: String,
    val artistImage: String,
    val trackUrl: String
){
    constructor() : this("","","","","")
}
