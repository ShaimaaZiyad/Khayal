package com.shaimaziyad.khayal.screens.profile

import android.app.Application
import androidx.lifecycle.*
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.SharePrefManager
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application)  : AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val sharePrefManager = SharePrefManager(application)


    private val _userStatus= MutableLiveData<Boolean>()
    val userStatus: LiveData<Boolean> = _userStatus


    private val _isLoggedOut= MutableLiveData<Boolean>()
    val isLoggedOut: LiveData<Boolean> = _isLoggedOut




    private val _user= MutableLiveData<User>()
    val user: LiveData<User> = _user



    // todo: enhance this this function
    fun signOut(){
        viewModelScope.launch {
            userRepository.signOut()
            sharePrefManager.signOut()
            _isLoggedOut.value = true
        }
    }


//    init {
//        viewModelScope.launch {
//            _user.value = FirebaseProfileService.getProfileData()
//
//        }
//    }
}
