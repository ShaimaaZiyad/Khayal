package com.shaimaziyad.khayal1.screens.bannerManager

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal1.data.Banner
import com.shaimaziyad.khayal1.repository.BannerRepository
import com.shaimaziyad.khayal1.utils.DataStatus
import com.shaimaziyad.khayal1.utils.Result
import com.shaimaziyad.khayal1.utils.getCurrentTime
import kotlinx.coroutines.launch


class BannerManagerViewModel(private val bannerRepo: BannerRepository) : ViewModel() {

    companion object {
        private const val TAG = "BannerManagerViewModel"
    }

    private val _banners = MutableLiveData<List<Banner>?>()
    val banners: LiveData<List<Banner>?> = _banners

    private val _loadStatus = MutableLiveData<DataStatus?>()
    val loadStatus: LiveData<DataStatus?> = _loadStatus

    private val _uploadStatus = MutableLiveData<DataStatus?>()
    val uploadStatus: LiveData<DataStatus?> = _uploadStatus

    private val _updateStatus = MutableLiveData<DataStatus?>()
    val updateStatus: LiveData<DataStatus?> = _updateStatus

    private val _deleteStatus = MutableLiveData<DataStatus?>()
    val deleteStatus: LiveData<DataStatus?> = _deleteStatus

    private val _info = MutableLiveData<String?>()
    val info: LiveData<String?> = _info

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isDataExist = MutableLiveData<Boolean?>()
    val isDataExist: LiveData<Boolean?> = _isDataExist


    init {
        loadAds()
    }


    fun loadAds() {
        Log.d(TAG, "onLoading.. banners")
        resetStatus()
        _loadStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = bannerRepo.loadBanners()
            if (res is Result.Success) {
                Log.d(TAG, "onSuccess: banners: ${res.data.size}")
                _loadStatus.value = DataStatus.SUCCESS
                val data = res.data
                _banners.value = data
                _isDataExist.value = data.isEmpty()
            } else if (res is Result.Error) {
                Log.d(
                    TAG,
                    "onError: error happen during fetching banners due to ${res.exception.message}"
                )
                _loadStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }


    fun uploadBanner(banner: Banner) {
        resetStatus()
        Log.d(TAG, "onLoading: adding banner")
        _uploadStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val image = banner.cover.toUri()
            val fileName = getCurrentTime().toString()
            val imageCover = bannerRepo.uploadBannerCover(image, fileName)
            banner.cover = imageCover
            val res = bannerRepo.submitBanner(banner)
            if (res is Result.Success) {
                Log.d(TAG, "onSuccess: banner has been added success")
                _uploadStatus.value = DataStatus.SUCCESS

                // update local banners
                val banners = _banners.value?.toMutableList()
                banners?.add(banner)
                _banners.value = banners

            } else if (res is Result.Error) {
                Log.d(
                    TAG,
                    "onError: error happen during adding banner due to ${res.exception.message}"
                )
                _uploadStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }

    fun updateBanner(banner: Banner) {
        resetStatus()
        Log.d(TAG, "onLoading: update banner")
        _updateStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val image = banner.cover.toUri()
            val fileName = getCurrentTime().toString()
            val imageCover = bannerRepo.uploadBannerCover(image, fileName)
            banner.cover = imageCover

            val res = bannerRepo.updateBanner(banner)
            if (res is Result.Success) {
                Log.d(TAG, "onSuccess: banner has been updated success")
                _updateStatus.value = DataStatus.SUCCESS

                // update local banner
                val banners = _banners.value?.toMutableList()
                val oldBanner = banners?.find { it.id == banner.id }!!
                banners.remove(oldBanner)
                banners.add(banner)
                _banners.value = banners

            } else if (res is Result.Error) {
                Log.d(
                    TAG,
                    "onError: error happen during updating banner due to ${res.exception.message}"
                )
                _updateStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }

    fun deleteBanner(banner: Banner) {
        resetStatus()
        Log.d(TAG, "onLoading: deleting banner")
        _deleteStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = bannerRepo.deleteBanner(banner)
            if (res is Result.Success) {
                Log.d(TAG, "onSuccess: banner has been deleted success")
                _deleteStatus.value = DataStatus.SUCCESS

                // remove local banners
                val banners = _banners.value?.toMutableList()
                banners?.remove(banner)
                _banners.value = banners

            } else if (res is Result.Error) {
                Log.d(
                    TAG,
                    "onError: error happen during deleting banner due to ${res.exception.message}"
                )
                _deleteStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }


    fun updateBannersSettings() {

    }


    fun resetStatus() {
        _loadStatus.value = null
        _uploadStatus.value = null
        _updateStatus.value = null
        _deleteStatus.value = null
        _info.value = null
        _error.value = null
        _isDataExist.value = null
    }


}