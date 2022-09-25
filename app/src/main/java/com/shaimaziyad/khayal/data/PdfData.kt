package com.shaimaziyad.khayal.data

import android.os.Parcelable
import com.shaimaziyad.khayal.utils.getCurrentTime
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class PdfData(
    var pdfId: String = "",
    var title: String = "",
    var url: String = "",
    var novelId: String = "",
    var createDate: Date = getCurrentTime(),

) : Parcelable{
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "pdfId" to pdfId,
            "title" to title,
            "url" to url,
            "novelId" to novelId,
            "createDate" to createDate,
            )
    }
}
