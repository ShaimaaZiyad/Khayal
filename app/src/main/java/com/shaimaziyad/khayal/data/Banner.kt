package com.shaimaziyad.khayal.data

import android.os.Parcelable
import com.shaimaziyad.khayal.utils.DisplayableHomeItem
import com.shaimaziyad.khayal.utils.getCurrentTime
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.HashMap

@Parcelize
data class Banner(
    val id: String = "",
    var title: String = "",
    var description: String = "",
    var cover: String = "",
    var type: String = "",
    @field:JvmField
    var isActive: Boolean = false,
    val date: Date = getCurrentTime()
) : DisplayableHomeItem, Parcelable {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "title" to title,
            "description" to description,
            "cover" to cover,
            "type" to type,
            "isActive" to isActive,
            "date" to date
        )
    }
}
