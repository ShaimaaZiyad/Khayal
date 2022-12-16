package com.shaimaziyad.khayal1.repository

import android.net.Uri
import com.shaimaziyad.khayal1.remote.DataBase
import com.shaimaziyad.khayal1.utils.FileType
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import com.shaimaziyad.khayal1.utils.Result.Success
import com.shaimaziyad.khayal1.utils.Result.Error
import com.shaimaziyad.khayal1.utils.Result

class PdfRepository(private val remote: DataBase) {

    suspend fun uploadPdfs(filesUri: List<Uri>) = remote.insertFiles(filesUri, FileType.PDF.name)

    suspend fun loadPdf(uri: String): Result<ByteArray> {
        return supervisorScope {
            val loadTask = async { remote.loadPdf(uri) }
            try {
                Success(loadTask.await())
            } catch (ex: Exception) {
                Error(Exception(ex))
            }
        }
    }

}