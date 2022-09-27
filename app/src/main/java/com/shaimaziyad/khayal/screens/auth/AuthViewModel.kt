package com.shaimaziyad.khayal.screens.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.DataStatus
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AuthViewModel:ViewModel(){

    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val userRepository = UserRepository()

    private val _registerStatus = MutableLiveData<DataStatus?>()
    val registerStatus: LiveData<DataStatus?> = _registerStatus

    private val _loginStatus = MutableLiveData<DataStatus?>()
    val loginStatus: LiveData<DataStatus?> = _loginStatus

    private val _isLogged = MutableLiveData<Boolean?>()
    val isLogged: LiveData<Boolean?> = _isLogged

    private val _isRegister = MutableLiveData<Boolean?>()
    val isRegister: LiveData<Boolean?> = _isRegister

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error




    fun register(user: User) {
        resetStatus()
        _registerStatus.value = DataStatus.LOADING
        Log.d(TAG,"onRegister: Loading...")
        viewModelScope.launch {
            userRepository.signUpWithEmail(user,
            onSuccess = {
                Log.d(TAG,"onRegister: Registration have been success...")
                _registerStatus.value = DataStatus.SUCCESS
                _isRegister.value = true
            },
            onError = { error->
                Log.d(TAG,"onRegister: Registration Failed due to ${error.message}")
                _isRegister.value = null
                _registerStatus.value = DataStatus.ERROR
                _error.value = error.message
            })
        }
    }



    fun login(email: String, password: String) {
        resetStatus()
        _loginStatus.value = DataStatus.LOADING
        Log.d(TAG,"onRegister: Loading...")
        viewModelScope.launch {
            userRepository.loginWithEmail(email,password,
                onSuccess = {
                    Log.d(TAG,"onRegister: Registration have been success...")
                    _loginStatus.value = DataStatus.SUCCESS
                    _isLogged.value = true
                },
                onError = { error->
                    Log.d(TAG,"onRegister: Registration Failed due to ${error.message}")
                    _loginStatus.value = DataStatus.ERROR
                    _isLogged.value = null
                    _error.value = error.message
                })
        }
    }


    fun setError(message: String){
        _error.value = message
    }




    private fun resetStatus(){
        _registerStatus.value = null
        _loginStatus.value = null
        _error.value = null
    }


    override fun onCleared() {
        super.onCleared()


        viewModelScope.cancel()
    }

}