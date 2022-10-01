package com.shaimaziyad.khayal.screens.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
import com.shaimaziyad.khayal.utils.SharePrefManager

class HomeViewModel(application: Application): AndroidViewModel(application) {

    companion object{
        private const val TAG = "HomeViewModel"
    }

    private val userRepository = UserRepository()
    private val novelRepository = NovelRepository()
    private val userType = SharePrefManager(application).loadUser().userType

    private val _novels = MutableLiveData<List<NovelData>?>()
    val novels: LiveData<List<NovelData>?> = _novels

    private val _novelsStatus = MutableLiveData<DataStatus?>()
    val novelsStatus: LiveData<DataStatus?> = _novelsStatus

    private val _info = MutableLiveData<String?>()
    val info: LiveData<String?> = _info

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _isCustomer = MutableLiveData<Boolean?>(isCustomer(userType))
    val isCustomer: LiveData<Boolean?> = _isCustomer

    private val _isDataExist = MutableLiveData<Boolean?>()
    val isDataExist: LiveData<Boolean?> = _isDataExist


    init {
        loadUser()
    }


    fun loadUser() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid!!
            _user.value = userRepository.loadUser(userId)
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




    private fun resetStatus(){
        _novelsStatus.value = null
    }

    fun searchByNovelTitle(query: String): List<NovelData> { // return list of novels
        val result = if (query.isNotEmpty()) {
            _novels.value?.filter { it.title.contains(query) }
        }else {
            _novels.value
        }
        return result!!
    }


}