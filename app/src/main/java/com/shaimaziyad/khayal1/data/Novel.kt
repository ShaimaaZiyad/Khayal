package com.shaimaziyad.khayal1.data

import android.os.Parcelable
import com.shaimaziyad.khayal1.utils.DisplayableHomeItem
import com.shaimaziyad.khayal1.utils.getCurrentTime
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Novel(
    var novelId: String = "",
    var title: String = "",
    var description: String = "",
    var type: String = "", // to save if novel فصحى ام عامية ام خليجية
    var category: String = "", // to save if it رواية ام قصة ام نوفيلا
    var writer: String = "",
    var cover: String = "",
    var createDate: Date = getCurrentTime(),
    var viewCount: List<String> = ArrayList(), // عدد الزيارات او المشاهدات
    var pdfs: List<String> = ArrayList(),
    var pdfsCount: Int = 0, // i need it to show in the cardView in home screen >>>> numberOfChapters

//    var continuousOrComplete: String,
//    var seriesOrSolo: String

) : Parcelable, DisplayableHomeItem {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "novelId" to novelId,
            "title" to title,
            "description" to description,
            "type" to type,
            "category" to category,
            "writer" to writer,
            "cover" to cover,
            "createDate" to createDate,
            "viewCount" to viewCount,
            "pdfs" to pdfs,
        )
    }
}

