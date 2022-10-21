package com.shaimaziyad.khayal.screens.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.notification.getToken
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.DataStatus
import com.shaimaziyad.khayal.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AuthViewModel(val userRepo: UserRepository): ViewModel(){

    companion object {
        private const val TAG = "AuthViewModel"
    }

//    private val userRepository = UserRepository()

    private val _registerStatus = MutableLiveData<DataStatus?>()
    val registerStatus: LiveData<DataStatus?> = _registerStatus

    private val _loginStatus = MutableLiveData<DataStatus?>()
    val loginStatus: LiveData<DataStatus?> = _loginStatus

    private val _resetPasswordStatus = MutableLiveData<DataStatus?>()
    val resetPasswordStatus: LiveData<DataStatus?> = _resetPasswordStatus

    private val _isLogged = MutableLiveData<User?>()
    val isLogged: LiveData<User?> = _isLogged

    private val _isRegister = MutableLiveData<Boolean?>()
    val isRegister: LiveData<Boolean?> = _isRegister

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    init {
//        resetStatus()
    }

    fun register(user: User) {
        resetStatus()
        _registerStatus.value = DataStatus.LOADING
        Log.d(TAG,"onRegister: Loading...")
        viewModelScope.launch {
            val res = userRepo.signUpWithEmail(user)
            if (res is Result.Success){
                Log.d(TAG,"onRegister: Registration have been success...")
                _registerStatus.value = DataStatus.SUCCESS
                _isRegister.value = true
                resetStatus()
            }else if( res is Result.Error){
                Log.d(TAG,"onRegister: Registration Failed due to ${res.exception.message}")
                _isRegister.value = null
                _registerStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
                resetStatus()
            }
        }
    }




    fun resetPassword(email: String) {
        resetStatus()
        _resetPasswordStatus.value = DataStatus.LOADING
        Log.d(TAG,"onResetPass: loading..")
        viewModelScope.launch {
            val res =  userRepo.resetPassword(email)
            if (res is Result.Success) {
                Log.d(TAG,"onResetPass: reset password have been success...")
                _resetPasswordStatus.value = DataStatus.SUCCESS
                resetStatus()
            }
            else if (res is Result.Error){
                Log.d(TAG,"onResetPass: reset password failed due to ${res.exception.message}")
                _resetPasswordStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
                resetStatus()
            }
        }
    }



    fun login(email: String, password: String,isRemOn: Boolean,context: Context) {
        resetStatus()
        _loginStatus.value = DataStatus.LOADING
        Log.d(TAG,"onLogin: Loading...")
        viewModelScope.launch {
            val res =  userRepo.loginWithEmail(email, password, isRemOn,context)
            if (res is Result.Success) {
                Log.d(TAG,"onLogin: Login have been success...")
                _loginStatus.value = DataStatus.SUCCESS
                _isLogged.value = res.data
                resetStatus()
            }
            else if (res is Result.Error){
                Log.d(TAG,"onLogin: Login Failed due to ${res.exception.message}")
                _loginStatus.value = DataStatus.ERROR
                _isLogged.value = null
                _error.value = res.exception.message
                resetStatus()
            }
        }
    }



    fun setError(message: String) {
        _error.value = message
    }


    fun resetStatus(){
        _registerStatus.value = null
        _loginStatus.value = null
        _error.value = null
        _isLogged.value = null
        _isRegister.value = false
        _resetPasswordStatus.value = null
    }


    override fun onCleared() {
        super.onCleared()


        viewModelScope.cancel()
    }

}