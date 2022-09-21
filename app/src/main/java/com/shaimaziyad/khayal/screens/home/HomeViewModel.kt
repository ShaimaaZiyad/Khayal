package com.shaimaziyad.khayal.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.repository.NovelRepository
import com.shaimaziyad.khayal.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private val userRepository = UserRepository()
    private val novelRepository = NovelRepository()

    private val _novels = MutableLiveData<List<NovelData>?>()
    val novels: LiveData<List<NovelData>?> = _novels

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user




//    val novels = novelRepository.novels

    init {

//        loadNovels()
    }


    fun loadUser(userId: String) {
        viewModelScope.launch {
            _user.value = userRepository.loadUser(userId)
        }
    }

    fun loadNovels() {
        viewModelScope.launch {
            _novels.value = novelRepository.getNovels()
        }
    }




}