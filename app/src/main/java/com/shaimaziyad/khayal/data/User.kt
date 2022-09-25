package com.shaimaziyad.khayal.data

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import com.shaimaziyad.khayal.utils.UserType
import kotlinx.parcelize.Parcelize
import java.util.HashMap


@Parcelize
data class User (
    var uid: String = "",
    var email : String = "",
    var name : String = "",
    var profileImage : String = "",
    var userType: String = UserType.USER.name,
    var password: String = "",
    var timestamp: String = "",
    var likes: List<String> = ArrayList(), // id of novel data
    var reads: List<String> = ArrayList()


): Parcelable {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "profileImage" to profileImage,
            "userType" to userType,
            "timestamp" to timestamp,
            "likes" to likes,
            "reads" to reads
        )
    }
}