package com.shaimaziyad.khayal.screens.addEditNovel

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal.data.Novel
import com.shaimaziyad.khayal.repository.NovelRepository
import com.shaimaziyad.khayal.repository.PdfRepository
import com.shaimaziyad.khayal.utils.DataStatus
import kotlinx.coroutines.launch
import com.shaimaziyad.khayal.utils.Result
import java.util.Calendar

class AddEditNovelViewModel(
    private val novelRepo: NovelRepository,
    private val pdfRepo: PdfRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AddEditViewModel"
    }


//    private val novelRepository = NovelRepository()
//    private val pdfRepository = PdfRepository()

    private val _novelStatus = MutableLiveData<DataStatus?>()
    val novelStatus: LiveData<DataStatus?> = _novelStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _info = MutableLiveData<String?>()
    val info: LiveData<String?> = _info

    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?> = _navigateToHome


    val novel = MutableLiveData<Novel?>()
    val isEdit = MutableLiveData<Boolean?>()


    init {
        resetStatus()
    }


    fun uploadNovel(novel: Novel, pdfsUri: ArrayList<Uri>) {
        Log.d(TAG, "onUploadNovel: Loading..")
        resetStatus()
        _novelStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val cover = novel.cover.toUri()
            val coverName = Calendar.getInstance().time.toString()
            val pdfIds =
                pdfRepo.uploadPdfs(pdfsUri)  // here we will upload the pdfs then we will return the ids of pdfs
            val coverId = novelRepo.uploadCover(
                cover,
                coverName
            ) // here we will upload the cover then we will return the cover id.
            novel.cover = coverId
            novel.pdfs = pdfIds
            novel.pdfsCount = pdfIds.size // for pdf count

            val res = novelRepo.addNovel(novel)
            if (res is Result.Success) {
                Log.d(TAG, "onUploadNovel: upload has been success")
                _info.value = "Uploaded"
                _novelStatus.value = DataStatus.SUCCESS
                _navigateToHome.value = true
            } else if (res is Result.Error) {
                Log.d(TAG, "onUploadNovel: upload failed due to ${res.exception.message}")
                _error.value = res.exception.message
                _novelStatus.value = DataStatus.ERROR
            }
        }
    }


    fun updateNovel(novel: Novel, pdfsUri: ArrayList<Uri>) {
        Log.d(TAG, "onUpdateNovel: Loading..")
        resetStatus()
        _novelStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val cover = novel.cover.toUri()
            val coverName = Calendar.getInstance().time.toString()
            val pdfIds =
                pdfRepo.uploadPdfs(pdfsUri)  // here we will upload the pdfs then we will return the ids of pdfs
            val coverId = novelRepo.uploadCover(
                cover,
                coverName
            ) // here we will upload the cover then we will return the cover id.
            novel.cover = coverId
            novel.pdfs = pdfIds
            novel.pdfsCount = pdfIds.size // for pdf count
            val res = novelRepo.updateNovel(novel)
            if (res is Result.Success) {
                Log.d(TAG, "onUpdateNovel: update has been success")
                _info.value = "Updated"
                _novelStatus.value = DataStatus.SUCCESS
                _navigateToHome.value = true
            } else if (res is Result.Error) {
                Log.d(TAG, "onUpdateNovel: update failed due to ${res.exception.message}")
                _error.value = res.exception.message
                _novelStatus.value = DataStatus.ERROR
            }
        }
    }


    fun deleteNovel(novel: Novel) {
        Log.d(TAG, "onDeleteNovel: Loading..")
        resetStatus()
        _novelStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = novelRepo.deleteNovel(novel)
            if (res is Result.Success) {
                Log.d(TAG, "onDeleteNovel: delete has been success")
                _info.value = "Delete"
                _novelStatus.value = DataStatus.SUCCESS
                _navigateToHome.value = true
            } else if (res is Result.Error) {
                Log.d(TAG, "onDeleteNovel: delete failed due to ${res.exception.message}")
                _error.value = res.exception.message
                _novelStatus.value = DataStatus.ERROR
            }
        }
    }


    fun navigateToHomeDone() {
        _navigateToHome.value = null
    }


    fun resetStatus() {
        _navigateToHome.value = null
        _novelStatus.value = null
        _error.value = null
        _info.value = null
    }

}