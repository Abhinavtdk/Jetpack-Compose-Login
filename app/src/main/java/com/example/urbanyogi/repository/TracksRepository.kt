package com.example.urbanyogi.repository

import com.example.urbanyogi.TracksResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class TracksRepository {
    private val firestore = FirebaseFirestore.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTrackDetails() = callbackFlow {
        val collection = firestore.collection("tracks")
        val snapshotListener = collection.addSnapshotListener{ value, error ->
            val response = if(error==null){
                TracksResponse.onSuccess(value)
            }else{
                TracksResponse.onError(error)
            }

            offer(response)
        }

        awaitClose {
            snapshotListener.remove()
        }
    }
}