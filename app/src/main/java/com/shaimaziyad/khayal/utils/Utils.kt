package com.shaimaziyad.khayal.utils

import android.util.Log
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import java.util.*


enum class UserType { USER, ADMIN }

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */

val Result<*>.succeeded
    get() = this is Result.Success && data != null

//function to get pdf size
fun loadNovelSize(novelUrl: String , sizeTv: TextView) {
    val TAG = "NOVEL_SIZE_TAG"


    //using url, can get file and its me data from firebase storage
    val ref = FirebaseStorage.getInstance().getReferenceFromUrl(novelUrl)
    ref.metadata
        .addOnSuccessListener { storageMetadata ->
            Log.d(TAG, "LoadNovelSize: got metadata")
            val bytes = storageMetadata.sizeBytes.toDouble()
            Log.d(TAG, "loadNovelSize: Size Bytes $bytes")

            //convert bytes to KB/MB
            val kb = bytes / 1024
            val mb = kb / 1024
            if (mb >= 1) {
                sizeTv.text = "${String.format("%.2f", mb)} MB"
            } else if (kb >= 1) {
                sizeTv.text = "${String.format("%.2f", kb)} KB"
            } else {
                sizeTv.text = "${String.format("%.2f", bytes)} bytes"
            }
        }
        .addOnFailureListener { e ->
            //failed to get metadata
            Log.d(TAG, "loadNovelSize: Failed to get metadata due to ${e.message}")

        }
}

fun getCurrentTime(): Date = Calendar.getInstance().time