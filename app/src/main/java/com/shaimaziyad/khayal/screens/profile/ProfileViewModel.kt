package com.shaimaziyad.khayal.screens.profile

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.*
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepo: UserRepository): ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val localUser = userRepo.user


    private val _isCustomer = MutableLiveData<Boolean?>(isCustomer(localUser.userType))
    val isCustomer: LiveData<Boolean?> = _isCustomer

    private val _userStatus = MutableLiveData<DataStatus?>()
    val userStatus: LiveData<DataStatus?> = _userStatus

    private val _updateUserState = MutableLiveData<DataStatus?>()
    val updateUserState: LiveData<DataStatus?> = _updateUserState


    private val _isLoggedOut= MutableLiveData<Boolean?>()
    val isLoggedOut: LiveData<Boolean?> = _isLoggedOut

//    val user = MutableLiveData<User>()

    private val _user = MutableLiveData<User?>(localUser)
    val user: LiveData<User?> = _user


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error



    fun signOut() {
        viewModelScope.launch {
            val res = userRepo.signOut()
            if (res is Result.Success){
                _isLoggedOut.value = true
                _user.value = null
            }
        }
    }


    fun loadUser() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid!!
            Log.d(TAG,"user name: ${localUser.name}")
            _user.value = userRepo.loadUser(userId)
            _isCustomer.value = isCustomer(_user.value!!.userType)
        }
    }


    fun refreshUser(user: User){
        _user.value = user
    }



    fun update(oldUser: User) {
        Log.d(TAG,"onUpdating..")
        resetStatus()
        _updateUserState.value = DataStatus.LOADING
        viewModelScope.launch {
            val image = oldUser.profileImage.toUri()
            val fileName = getCurrentTime().toString()
            val imageProfile = userRepo.uploadProfile(image,fileName)
            oldUser.profileImage = imageProfile
            val res = userRepo.update(oldUser)
            if (res is Result.Success) {
                Log.d(TAG,"onUpdating: user has been updated")
                _updateUserState.value = DataStatus.SUCCESS
                _user.value = oldUser

            }else if (res is Result.Error) {
                Log.d(TAG,"onUpdating: faild to update due to ${res.exception.message}")
                _updateUserState.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }



    fun resetStatus() {
        _userStatus.value = null
        _updateUserState.value = null
        _isLoggedOut.value = null
    }


}
