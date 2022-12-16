package com.shaimaziyad.khayal1.data

import android.os.Parcelable
import com.shaimaziyad.khayal1.utils.getCurrentTime
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.HashMap

@Parcelize
data class Notification(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val icon: String = "",
    var externalLink: String = "", // if you want to send the user to any website , blog or facebook
    var internalLink: String = "", // if you want to send the user to any screen inside the app
    var targetUser: String? = "", // recipient id
    var type: String = "", // system , direct
    @field:JvmField
    var isRead: Boolean? = false,
    var pattern: String? = "",
    var from: String? = "", // sender id
    val createDate: Date = getCurrentTime()

) : Parcelable {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "title" to title,
            "description" to body,
            "icon" to icon,
            "externalLink" to externalLink,
            "internalLink" to internalLink,
            "targetUser" to targetUser!!,
            "type" to type,
            "isRead" to isRead!!,
            "pattern" to pattern!!,
            "from" to from!!,
            "createDate" to createDate
        )
    }
}
