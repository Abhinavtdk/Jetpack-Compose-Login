package com.example.urbanyogi.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.urbanyogi.repository.RegisterRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RegisterViewModel(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _signedIn = MutableLiveData(false)
    val signedIn: LiveData<Boolean> = _signedIn

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun removeError() {
        _error.value = null
    }

    fun signUp(email: String, password: String, confirmPassword: String) = viewModelScope.launch {
        try {
            _loading.postValue(true)
            if (confirmPassword != password) throw IllegalArgumentException("Passwords do not match")
            registerRepository.register(email, password)
            _signedIn.postValue(true)
        } catch (e: Exception) {
            _error.postValue(e.localizedMessage)
        } finally {
            _loading.postValue(false)
        }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        try {
            _loading.postValue(true)
            registerRepository.login(email, password)
            _signedIn.postValue(true)
            Log.d("LoginPage", "signIn: +${signedIn.value}")
        } catch (e: Exception) {
            _error.postValue(e.localizedMessage)
        } finally {
            _loading.postValue(false)
        }
    }

    fun signOut() {
        registerRepository.signOut()
        _signedIn.postValue(false)
    }

}

class RegisterViewModelFactory(
    private val registerRepository: RegisterRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}