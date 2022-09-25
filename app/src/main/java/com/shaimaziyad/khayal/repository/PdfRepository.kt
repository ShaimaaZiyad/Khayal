package com.shaimaziyad.khayal.repository

import android.net.Uri
import com.shaimaziyad.khayal.data.PdfData
import com.shaimaziyad.khayal.remote.DataBase

class PdfRepository {

    private val remote = DataBase()

    suspend fun addPdf(pdfData: PdfData) = remote.addPdf(pdfData)

    suspend fun deletePdf(pdfData: PdfData) = remote.deletePdf(pdfData)

    suspend fun getPdf(pdfUri: String) = remote.getPdf(pdfUri)

    suspend fun uploadFile(uri: Uri, fileName: String, fileType: String) = remote.uploadFile(uri, fileName, fileType)

}