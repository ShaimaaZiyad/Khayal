package com.shaimaziyad.khayal.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.UserRepository

class ProfileViewModel  : ViewModel() {

    private val userRepository = UserRepository()

    private val _user= MutableLiveData<User>()
    val user: LiveData<User> = _user

//    init {
//        viewModelScope.launch {
//            _user.value = FirebaseProfileService.getProfileData()
//
//        }
//    }
}
