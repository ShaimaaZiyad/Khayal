package com.shaimaziyad.khayal.screens.addEditNovel

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.repository.NovelRepository
import com.shaimaziyad.khayal.repository.PdfRepository
import com.shaimaziyad.khayal.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.shaimaziyad.khayal.utils.Result
import java.util.Calendar

class AddEditNovelViewModel(): ViewModel() {

    companion object{
        private const val TAG = "AddEditViewModel"
    }


    private val novelRepository = NovelRepository()
    private val pdfRepository = PdfRepository()

    private val _novelStatus = MutableLiveData<DataStatus?>()
    val novelStatus: LiveData<DataStatus?> = _novelStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?> = _navigateToHome


    val novel = MutableLiveData<NovelData?>()
    val isEdit = MutableLiveData<Boolean?>()







    fun uploadNovel(novel: NovelData, pdfsUri: ArrayList<Uri>) {
        Log.d(TAG,"onUploadNovel: Loading..")
        resetStatus()
        _novelStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val cover = novel.cover.toUri()
            val coverName = Calendar.getInstance().time.toString()
            val pdfIds = pdfRepository.uploadPdfs(pdfsUri)  // here we will upload the pdfs then we will return the ids of pdfs
            val coverId = novelRepository.uploadCover(cover,coverName) // here we will upload the cover then we will return the cover id.
            novel.cover = coverId
            novel.pdfs = pdfIds
            novel.pdfsCount = pdfIds.size // for pdf count

            val res = novelRepository.addNovel(novel)
            if (res is Result.Success){
                Log.d(TAG,"onUploadNovel: upload has been success")
                _novelStatus.value = DataStatus.SUCCESS
                _navigateToHome.value = true
            }
            else if (res is Result.Error){
                Log.d(TAG,"onUploadNovel: upload failed due to ${res.exception.message}")
                _error.value = res.exception.message
                _novelStatus.value = DataStatus.ERROR
            }
        }
    }



    fun updateNovel(novel: NovelData) {
        viewModelScope.launch {
            // TODO: still under working
        }
    }


    fun deleteNovel (novel: NovelData) {
        viewModelScope.launch {
            // TODO: still under working
        }
    }


    fun navigateToHomeDone() {
        _navigateToHome.value = null
    }


    private fun resetStatus() {
        _navigateToHome.value = null
        _novelStatus.value = null
        _error.value = null
    }

}