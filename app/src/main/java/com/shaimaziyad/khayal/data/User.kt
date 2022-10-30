package com.shaimaziyad.khayal.data

import android.os.Parcelable
import com.shaimaziyad.khayal.utils.UserType
import com.shaimaziyad.khayal.utils.getCurrentTime
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList


@Parcelize
data class User(
    var uid: String = "",
    var email: String = "",
    var name: String = "",
    var profileImage: String = "",
    var userType: String = UserType.USER.name,
    var password: String = "",
    var createData: Date? = getCurrentTime(),
    var likes: List<String> = ArrayList(), // id of novel data
    var reads: List<String> = ArrayList(),
    var notifications: List<String>? = ArrayList(),
    var token: String? = ""


) : Parcelable {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "profileImage" to profileImage,
            "userType" to userType,
            "createData" to createData!!,
            "likes" to likes,
            "reads" to reads,
            "notifications" to notifications!!,
            "token" to token!!
        )
    }
}