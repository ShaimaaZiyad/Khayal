package com.shaimaziyad.khayal1.utils

import android.content.Context
import com.shaimaziyad.khayal1.R
import java.util.*
import kotlin.collections.HashMap

const val ERR_UPLOAD = "UploadErrorException"

enum class UserType { USER, ADMIN }

enum class DataStatus { LOADING, ERROR, SUCCESS }

enum class FileType { PDF, IMAGE, IMAGE_PROFILE, IMAGE_NOTIFICATION, IMAGE_BANNER }

enum class NotifyType { System, Direct }

enum class NotifyPattern { Message, Report, Alert }

enum class SelectedSection { UnRead, Read, System }

enum class AdType { Rotating, Banner }


class NovelFilter(private val context: Context) {

    private fun getString(res: Int) = context.getString(res)

    val novelCategories = hashMapOf(
        getString(R.string.fosha) to 0, // فصحى
        getString(R.string.slang) to 1, // عامية
        getString(R.string.gulf) to 2, // خليجية

    )

    val novelType = hashMapOf(
        getString(R.string.novel) to 0, // رواية
        getString(R.string.story) to 1, // قصة
        getString(R.string.novella) to 2 // نوفيلا
    )


}


fun getInternalLinks(context: Context): HashMap<String, Int> {
    return hashMapOf(
        context.getString(R.string.home) to R.id.home,
        context.getString(R.string.profile) to R.id.profile
    )
}


fun getCurrentTime(): Date = Calendar.getInstance().time


internal fun getNovelId() = "novId" + UUID.randomUUID().toString()
internal fun getPdfId() = "pdfId" + UUID.randomUUID().toString()
internal fun getNotifyId() = "notify" + UUID.randomUUID().toString()
internal fun getBannerId() = "banner" + UUID.randomUUID().toString()
