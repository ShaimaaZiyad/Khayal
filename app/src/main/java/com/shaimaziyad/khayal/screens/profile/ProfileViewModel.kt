package com.shaimaziyad.khayal.screens.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.DataStatus
import com.shaimaziyad.khayal.utils.SharePrefManager
import com.shaimaziyad.khayal.utils.isCustomer
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application)  : AndroidViewModel(application) {

    companion object {
        private const val TAG = "ProfileViewModel"
    }


    private val userRepository = UserRepository()
    private val sharePrefManager = SharePrefManager(application)


    private val _userStatus = MutableLiveData<DataStatus?>()
    val userStatus: LiveData<DataStatus?> = _userStatus


    private val _isLoggedOut= MutableLiveData<Boolean>()
    val isLoggedOut: LiveData<Boolean> = _isLoggedOut

    private val _user= MutableLiveData<User>()
    val user: LiveData<User> = _user



    init {
        loadUser()
    }

    // todo: enhance this this function
    fun signOut(){
        viewModelScope.launch {
            userRepository.signOut()
            sharePrefManager.signOut()
            _isLoggedOut.value = true
        }
    }



    // todo: need to enhance
    fun loadUser() {
        Log.d(TAG,"onUserLoading..")
        resetStatus()
        _userStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid!!
            _user.value = userRepository.loadUser(userId)
            _userStatus.value = DataStatus.SUCCESS
            Log.d(TAG,"onLoadUser: load data have been success")
        }
    }



    fun resetStatus(){
        _userStatus.value = null
    }


}
