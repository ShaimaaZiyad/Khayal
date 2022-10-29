//package com.shaimaziyad.khayal.screens.novelDetails
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.auth.FirebaseAuth
//import com.shaimaziyad.khayal.data.Novel
//import com.shaimaziyad.khayal.repository.UserRepository
//import com.shaimaziyad.khayal.screens.home.HomeViewModel
//import com.shaimaziyad.khayal.utils.DataStatus
//import com.shaimaziyad.khayal.utils.Result
//import kotlinx.coroutines.launch
//
//class NovelDetailsViewModel(private val userRepo: UserRepository): ViewModel() {
//
//
//    companion object{
//        private const val TAG = "NovelDetailsViewModel"
//    }
//
//
//    private val _likeStatus = MutableLiveData<DataStatus?>()
//    val likeStatus: LiveData<DataStatus?> = _likeStatus
//
//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> = _error
//
//
//    fun setLikeToNovel(novel: Novel) {
//        Log.d(TAG,"onLoading.. setting like to novel..")
//        resetStatus()
//        _likeStatus.value = DataStatus.LOADING
//        viewModelScope.launch {
//            val userId = FirebaseAuth.getInstance().currentUser?.uid!!
//            val res = userRepo.setLikeToNovel(novel,userId)
//            if (res is Result.Success){
//                Log.d(TAG,"onSuccess: like has been added success")
//                _likeStatus.value = DataStatus.SUCCESS
//            }
//            else if(res is Result.Error) {
//                Log.d(TAG,"onError: error happen setting like due to ${res.exception.message}")
//                _likeStatus.value = DataStatus.ERROR
//                _error.value = res.exception.message
//
//            }
//
//        }
//    }
//
//
//
//
//
//    fun resetStatus() {
//        _likeStatus.value = null
//    }
//
//
//}