package com.shaimaziyad.khayal.screens.home


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Banner
import com.shaimaziyad.khayal.data.Novel
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.repository.NovelRepository
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.*
import kotlinx.coroutines.launch
import com.shaimaziyad.khayal.utils.Result.Success
import com.shaimaziyad.khayal.utils.Result.Error
@SuppressLint("StaticFieldLeak")
class HomeViewModel(
    val context: Context,
    private val userRepo: UserRepository,
    private val novelRepo: NovelRepository): ViewModel() {

    companion object{
        private const val TAG = "HomeViewModel"
    }

    private val localUser = userRepo.user

    private val _novels = MutableLiveData<List<Novel>?>()
    val novels: LiveData<List<Novel>?> = _novels

    private val _banners = MutableLiveData<List<Banner>?>()
    val banners: LiveData<List<Banner>?> = _banners

    private val _mixedData = MutableLiveData<List<DisplayableHomeItem>?>()
    val mixedData: LiveData<List<DisplayableHomeItem>?> = _mixedData

    private val _novelsStatus = MutableLiveData<DataStatus?>()
    val novelsStatus: LiveData<DataStatus?> = _novelsStatus


    private val _info = MutableLiveData<String?>()
    val info: LiveData<String?> = _info

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

//    private val _isCustomer = MutableLiveData<Boolean?>(isCustomer(localUser.userType))
//    val isCustomer: LiveData<Boolean?> = _isCustomer

    private val _isDataExist = MutableLiveData<Boolean?>()
    val isDataExist: LiveData<Boolean?> = _isDataExist



    init {
        loadNovels()
    }


    // todo: load the ads then load novels

    fun loadNovels() {
        Log.d(TAG,"onLoading.. Novels")
        resetStatus()
        _novelsStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = novelRepo.loadNovels()
            if (res is Success){
                Log.d(TAG,"onSuccess: novels size: ${res.data.size}")
                _novelsStatus.value = DataStatus.SUCCESS
                val data = res.data
                _novels.value = data.sortedBy { it.createDate }
                _isDataExist.value = _novels.value.isNullOrEmpty()
                setNovels()

            }
            else if(res is Error) {
                Log.d(TAG,"onError: error happen during fetching novels due to ${res.exception.message}")
                _novelsStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
                _isDataExist.value = null

            }

        }
    }




    private fun setNovels() {
        val list = ArrayList<DisplayableHomeItem>()
        val novels = _novels.value!!
        val categories = _novels.value?.map { it.category }?.distinct()
        val types = _novels.value?.map { it.type }?.distinct()

        // set banner
//        list.add(Banner("1","Ad number one","hi this is sub title","welcome this is description", R.drawable.ad_one)) // banner type one

        // set novels by category
        categories?.forEach { category ->
            val filteredNovels = filterByCategory(category).shuffled()
            Log.d(TAG,"category:${getNovelCategoryByKey(context,category.toInt())}")
            list.add(NovelData(getNovelCategoryByKey(context,category.toInt()),filteredNovels))
        }

        // set novels by type
        types?.forEach { type->
            val filteredNovels = filterByType(type).shuffled()
            Log.d(TAG,"category:${getNovelTypeKey(context,type.toInt())} ")
            list.add(NovelData(getNovelTypeKey(context,type.toInt()),filteredNovels))
        }

//        // set liked novels
//        val likedNovels = ArrayList<Novel>()
//        val liked = localUser.likes
//        liked.forEach { novelId ->
//            val novel = novels.find { it.novelId == novelId }
//            if (novel != null){
//                likedNovels.add(novel)
//            }
//        }
//        list.add(NovelData(context.getString(R.string.favorite_books),likedNovels))



        _mixedData.value = list.shuffled()
    }



    fun resetStatus() {
        _novelsStatus.value = null
        _info.value = null
        _error.value = null
    }


    fun filterByCategory(category: String): List<Novel> {
        val result = if (category.isNotEmpty()) {
            _novels.value?.filter { it.category == category }
        }else {
            _novels.value
        }
        return result!!
    }


    fun filterByType(category: String): List<Novel> {
        val result = if (category.isNotEmpty()) {
            _novels.value?.filter { it.type == category }
        }else {
            _novels.value
        }
        return result!!
    }



    fun searchByNovelTitle(query: String): List<Novel> { // return list of novels
        val result = if (query.isNotEmpty()) {
            _novels.value?.filter { it.title.lowercase().contains(query.lowercase()) }
        }else {
            _novels.value
        }
        return result!!
    }


}