package com.shaimaziyad.khayal.screens.pdfViewer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaimaziyad.khayal.repository.PdfRepository
import com.shaimaziyad.khayal.utils.DataStatus
import com.shaimaziyad.khayal.utils.Result
import kotlinx.coroutines.launch

class PdfViewerViewModel(private val pdfRepo: PdfRepository) : ViewModel() {

    companion object {
        private const val TAG = "PdfViewerViewModel"
    }

    private val _pdf = MutableLiveData<ByteArray?>()
    val pdf: LiveData<ByteArray?> = _pdf

    private val _pdfStatus = MutableLiveData<DataStatus?>()
    val pdfStatus: LiveData<DataStatus?> = _pdfStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    fun loadPdf(uri: String) {
        Log.d(TAG, "onLoadPdf: Loading")
        resetStatus()
        _pdfStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = pdfRepo.loadPdf(uri)
            if (res is Result.Success) {
                Log.d(TAG, "onLoadPdf: load pdf success")
                _pdfStatus.value = DataStatus.SUCCESS
                _pdf.value = res.data
            } else if (res is Result.Error) {
                Log.d(TAG, "onLoadPdf: load pdf Failed due to ${res.exception.message}")
                _pdfStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }


    fun resetStatus() {
        _pdfStatus.value = null
        _pdf.value = null
    }


}