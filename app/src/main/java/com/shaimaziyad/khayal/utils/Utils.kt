package com.shaimaziyad.khayal.utils

import android.util.Log
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import java.util.*


const val ERR_UPLOAD = "UploadErrorException"

enum class UserType { USER, ADMIN }

enum class DataStatus { LOADING, ERROR, SUCCESS }

enum class FileType {PDF, IMAGE}

fun getCurrentTime(): Date = Calendar.getInstance().time




internal fun getNovelId() = "novId" + UUID.randomUUID().toString()
internal fun getPdfId() = "pdfId" + UUID.randomUUID().toString()
