package com.shaimaziyad.khayal1.screens.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal1.data.User
import com.shaimaziyad.khayal1.notification.getToken
import com.shaimaziyad.khayal1.repository.UserRepository
import com.shaimaziyad.khayal1.utils.DataStatus
import com.shaimaziyad.khayal1.utils.Result
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AuthViewModel(val userRepo: UserRepository) : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }


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




    fun register(user: User) {
        resetStatus()
        _registerStatus.value = DataStatus.LOADING
        Log.d(TAG, "onRegister: Loading...")
        viewModelScope.launch {
            val res = userRepo.signUpWithEmail(user)
            if (res is Result.Success) {
                Log.d(TAG, "onRegister: Registration have been success...")
                _registerStatus.value = DataStatus.SUCCESS
                _isRegister.value = true

            } else if (res is Result.Error) {
                Log.d(TAG, "onRegister: Registration Failed due to ${res.exception.message}")
                _isRegister.value = null
                _registerStatus.value = DataStatus.ERROR
                _error.value = res.exception.message

            }
        }
    }


    fun resetPassword(email: String) {
        resetStatus()
        _resetPasswordStatus.value = DataStatus.LOADING
        Log.d(TAG, "onResetPass: loading..")
        viewModelScope.launch {
            val res = userRepo.resetPassword(email)
            if (res is Result.Success) {
                Log.d(TAG, "onResetPass: reset password have been success...")
                _resetPasswordStatus.value = DataStatus.SUCCESS

            } else if (res is Result.Error) {
                Log.d(TAG, "onResetPass: reset password failed due to ${res.exception.message}")
                _resetPasswordStatus.value = DataStatus.ERROR
                _error.value = res.exception.message

            }
        }
    }


    fun login(email: String, password: String, isRemOn: Boolean) {
        resetStatus()
        _loginStatus.value = DataStatus.LOADING
        Log.d(TAG, "onLogin: Loading...")
        viewModelScope.launch {
            val res = userRepo.loginWithEmail(email, password, isRemOn)
            if (res is Result.Success) {
                Log.d(TAG, "onLogin: Login have been success...")
                _loginStatus.value = DataStatus.SUCCESS
                _isLogged.value = res.data

            } else if (res is Result.Error) {
                Log.d(TAG, "onLogin: Login Failed due to ${res.exception.message}")
                _loginStatus.value = DataStatus.ERROR
                _isLogged.value = null
                _error.value = res.exception.message

            }
        }
    }


    // todo: enhance this
    fun refreshToken(user: User?) {
        if (user != null){
            getToken {
                viewModelScope.launch {
                    Log.d(TAG,"refresh user token")
                    user.token = it
                    userRepo.update(user)
                }
            }
        }
    }



    fun resetStatus() {
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