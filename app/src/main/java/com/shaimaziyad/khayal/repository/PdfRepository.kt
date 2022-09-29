package com.shaimaziyad.khayal.repository

import android.net.Uri
import com.shaimaziyad.khayal.remote.DataBase
import com.shaimaziyad.khayal.utils.FileType

class PdfRepository {

    private val remote = DataBase()

//    suspend fun addPdf(pdfData: PdfData) = remote.addPdf(pdfData)

    suspend fun uploadPdfs(filesUri: List<Uri>) = remote.insertFiles(filesUri,FileType.PDF.name)

//    suspend fun deletePdf(pdfData: PdfData) = remote.deletePdf(pdfData)
//
//    suspend fun getPdf(pdfUri: String) = remote.getPdf(pdfUri)
//
//    suspend fun uploadFile(uri: Uri, fileName: String, fileType: String) = remote.uploadFile(uri, fileName, fileType)

}