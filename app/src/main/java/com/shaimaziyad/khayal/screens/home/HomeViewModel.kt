package com.shaimaziyad.khayal.screens.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.NovelRepository
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.*
import kotlinx.coroutines.launch
import com.shaimaziyad.khayal.utils.Result.Success
import com.shaimaziyad.khayal.utils.Result.Error

class HomeViewModel(
    private val userRepo: UserRepository,
    private val novelRepo: NovelRepository
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val localUser = userRepo.user!!


    private val _novels = MutableLiveData<List<NovelData>?>()
    val novels: LiveData<List<NovelData>?> = _novels

    private val _novelsStatus = MutableLiveData<DataStatus?>()
    val novelsStatus: LiveData<DataStatus?> = _novelsStatus

    private val _info = MutableLiveData<String?>()
    val info: LiveData<String?> = _info

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    private val _isCustomer = MutableLiveData<Boolean?>(isCustomer(localUser.userType))
    val isCustomer: LiveData<Boolean?> = _isCustomer

    private val _isDataExist = MutableLiveData<Boolean?>()
    val isDataExist: LiveData<Boolean?> = _isDataExist


    fun loadNovels() {
        Log.d(TAG, "onLoading.. Novels")
        resetStatus()
        _novelsStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = novelRepo.loadNovels()
            if (res is Success) {
                Log.d(TAG, "onSuccess: novels size: ${res.data.size}")
                _novelsStatus.value = DataStatus.SUCCESS
                val data = res.data
                _novels.value = data.sortedBy { it.createDate }
                _isDataExist.value = _novels.value.isNullOrEmpty()
//                resetStatus()
            } else if (res is Error) {
                Log.d(
                    TAG,
                    "onError: error happen during fetching novels due to ${res.exception.message}"
                )
                _novelsStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
                _isDataExist.value = null
                resetStatus()
            }

        }
    }


    fun resetStatus() {
        _novelsStatus.value = null
        _info.value = null
        _error.value = null
    }

    fun searchByNovelTitle(query: String): List<NovelData> { // return list of novels
        val result = if (query.isNotEmpty()) {
            _novels.value?.filter { it.title.lowercase().contains(query.lowercase()) }
        } else {
            _novels.value
        }
        return result!!
    }


}