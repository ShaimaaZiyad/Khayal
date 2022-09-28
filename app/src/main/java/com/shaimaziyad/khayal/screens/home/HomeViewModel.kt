package com.shaimaziyad.khayal.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.NovelRepository
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.DataStatus
import com.shaimaziyad.khayal.utils.isCustomer
import kotlinx.coroutines.launch
import com.shaimaziyad.khayal.utils.Result
import com.shaimaziyad.khayal.utils.Result.Success
import com.shaimaziyad.khayal.utils.Result.Error

class HomeViewModel: ViewModel() {

    companion object{
        private const val TAG = "HomeViewModel"
    }

    private val userRepository = UserRepository()
    private val novelRepository = NovelRepository()

    private val _novels = MutableLiveData<List<NovelData>?>()
    val novels: LiveData<List<NovelData>?> = _novels

    private val _novelsStatus =MutableLiveData<DataStatus?>()
    val novelsStatus: LiveData<DataStatus?> = _novelsStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _isCustomer = MutableLiveData<Boolean?>()
    val isCustomer: LiveData<Boolean?> = _isCustomer

    private val _isDataExist = MutableLiveData<Boolean?>()
    val isDataExist: LiveData<Boolean?> = _isDataExist


    init {
        loadUser()
        loadNovels()

    }


    fun loadUser() {
        viewModelScope.launch {
            _user.value = userRepository.loadUser()
            _isCustomer.value = isCustomer(_user.value!!.userType)
        }
    }


    fun loadNovels() {
        Log.d(TAG,"onLoading.. Novels")
        resetStatus()
        _novelsStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = novelRepository.loadNovels()
            if (res is Success){
                Log.d(TAG,"onSuccess: novels size: ${res.data}")
                _novelsStatus.value = DataStatus.SUCCESS
                val data = res.data
                _novels.value = data
                _isDataExist.value = _novels.value.isNullOrEmpty()
            }
            else if(res is Error) {
                Log.d(TAG,"onError: error happen during fetching novels due to ${res.exception.message}")
                _novelsStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
                _isDataExist.value = null
            }

        }
    }






    fun resetStatus(){
        _novelsStatus.value = null
    }




}