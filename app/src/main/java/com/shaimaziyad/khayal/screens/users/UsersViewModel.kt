package com.shaimaziyad.khayal.screens.users


import android.util.Log
import androidx.lifecycle.*
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.DataStatus
import com.shaimaziyad.khayal.utils.Result
import kotlinx.coroutines.launch


class UsersViewModel(private val userRepo: UserRepository) : ViewModel() {

    companion object {
        private const val TAG = "UsersScreenViewModel"
    }


    private val _users = MutableLiveData<List<User>?>()
    val users: LiveData<List<User>?> = _users

    private val _usersStatus = MutableLiveData<DataStatus?>()
    val usersStatus: LiveData<DataStatus?> = _usersStatus


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    private val _isDataExist = MutableLiveData<Boolean?>()
    val isDataExist: LiveData<Boolean?> = _isDataExist


    init {
        loadUsers()
    }


    fun loadUsers() {
        Log.d(TAG, "onLoading.. Users")
        resetStatus()
        _usersStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = userRepo.loadUsers()
            if (res is Result.Success) {
                Log.d(TAG, "onSuccess: users size: ${res.data.size}")
                val data = res.data
                _users.value = data
                _usersStatus.value = DataStatus.SUCCESS
                _isDataExist.value = _users.value.isNullOrEmpty()
            } else if (res is Result.Error) {
                Log.d(
                    TAG,
                    "onError: error happen during fetching users due to ${res.exception.message}"
                )
                _usersStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
                _isDataExist.value = null
            }
        }

    }


    private fun resetStatus() {
        _usersStatus.value = null
    }


    fun searchByUserName(query: String): List<User> {  // return list of users
        val result = if (query.isNotEmpty()) {
            _users.value?.filter { it.name.lowercase().contains(query.lowercase()) }
        } else {
            _users.value
        }
        return result!!
    }


}