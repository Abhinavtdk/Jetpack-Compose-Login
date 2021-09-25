package com.example.urbanyogi.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterRepository {
    suspend fun register(email:String, password: String) : Unit = withContext(IO){
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun login(email: String,password: String) : Unit = withContext(IO){
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    fun signOut() = Firebase.auth.signOut()
}