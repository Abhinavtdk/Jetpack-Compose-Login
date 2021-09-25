package com.example.urbanyogi

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.lang.Exception

sealed class TracksResponse {
    data class onSuccess(val querySnapshot: QuerySnapshot?) : TracksResponse()
    data class onError(val exception: FirebaseFirestoreException?) : TracksResponse()
}