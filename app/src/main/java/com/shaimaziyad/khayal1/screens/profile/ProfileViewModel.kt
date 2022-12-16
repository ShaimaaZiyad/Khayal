package com.shaimaziyad.khayal1.screens.profile

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.shaimaziyad.khayal1.data.Novel
import com.shaimaziyad.khayal1.data.User
import com.shaimaziyad.khayal1.repository.UserRepository
import com.shaimaziyad.khayal1.utils.*
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepo: UserRepository) : ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val localUser = userRepo.user

    private val _isCustomer = MutableLiveData<Boolean?>(isCustomer(localUser.userType))
    val isCustomer: LiveData<Boolean?> = _isCustomer

    private val _updateUserState = MutableLiveData<DataStatus?>()
    val updateUserState: LiveData<DataStatus?> = _updateUserState


    private val _isLoggedOut = MutableLiveData<Boolean?>()
    val isLoggedOut: LiveData<Boolean?> = _isLoggedOut


    private val _user = MutableLiveData<User?>(localUser)
    val user: LiveData<User?> = _user


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _likeStatus = MutableLiveData<DataStatus?>()
    val likeStatus: LiveData<DataStatus?> = _likeStatus


    fun signOut() {
        viewModelScope.launch {
            val res = userRepo.signOut()
            if (res is Result.Success) {
                _isLoggedOut.value = true
                _user.value = null
            }
        }
    }


    fun loadUser() {
        viewModelScope.launch {
            _user.value = userRepo.loadUser()
            _isCustomer.value = isCustomer(_user.value!!.userType)
        }
    }


    fun isNovelLiked(novelId: String) = _user.value?.likes?.contains(novelId)

    fun refreshUser(user: User) {
        _user.value = user
    }


    fun setLikeToNovel(novel: Novel) {
        Log.d(TAG, "onLoading.. setting like to novel..")
        resetStatus()
        _likeStatus.value = DataStatus.LOADING
        viewModelScope.launch {

            val res = userRepo.setLikeToNovel(novel)
            if (res is Result.Success) {
                Log.d(TAG, "onSuccess: like status updated")
                _likeStatus.value = DataStatus.SUCCESS
                refreshLikes(novel)
            } else if (res is Result.Error) {
                Log.d(TAG, "onError: error happen setting like due to ${res.exception.message}")
                _likeStatus.value = DataStatus.ERROR
                _error.value = res.exception.message

            }
        }
    }


    // update likes locally
    private fun refreshLikes(novel: Novel) {
        val user = _user.value!!
        val likes = _user.value?.likes?.toMutableList()
        val isLiked = likes?.contains(novel.novelId)!!
        if (!isLiked) {
            likes.add(novel.novelId)
        } else {
            likes.remove(novel.novelId)
        }
        user.likes = likes
        refreshUser(user)
    }


    fun update(oldUser: User) {
        Log.d(TAG, "onUpdating..")
        resetStatus()
        _updateUserState.value = DataStatus.LOADING
        viewModelScope.launch {
            val image = oldUser.profileImage.toUri()
            val fileName = getCurrentTime().toString()
            val imageProfile = userRepo.uploadProfile(image, fileName)
            oldUser.profileImage = imageProfile
            val res = userRepo.update(oldUser)
            if (res is Result.Success) {
                Log.d(TAG, "onUpdating: user has been updated")
                _updateUserState.value = DataStatus.SUCCESS
                _user.value = oldUser

            } else if (res is Result.Error) {
                Log.d(TAG, "onUpdating: faild to update due to ${res.exception.message}")
                _updateUserState.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }


    fun resetStatus() {
        _updateUserState.value = null
        _isLoggedOut.value = null
    }


}
