package com.shaimaziyad.khayal.data

import android.os.Parcelable
import com.shaimaziyad.khayal.utils.getCurrentTime
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class NovelData(
    var novelId: String = "",
    var title: String = "",
    var description: String = "",
    var type: String = "", // ......?
    var category: String, // ....?
    var writer: String = "",
    var cover: String = "",
    var createDate: Date = getCurrentTime(),
    var viewCount: Long = 0, // .. ?
//    var isFavorite: Boolean = false,
    var pdfs: List<PdfData> = ArrayList()

): Parcelable {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "novelId" to novelId,
            "title" to title,
            "description" to description,
            "writer" to writer,
            "cover" to cover,
//            "categoryId" to categoryId,
            "createDate" to createDate,
//            "pdfsCount" to pdfsCount,
            "viewCount" to viewCount,
//            "isFavorite" to isFavorite,
            "pdfs" to pdfs,

        )
    }
}

