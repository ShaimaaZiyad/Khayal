package com.shaimaziyad.khayal.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage


/**    this file for handling the UI elements in general   **/


private const val TAG = "UI Extensions"

//function to get pdf size
fun loadNovelSize(novelUrl: String , sizeTv: TextView) {

    //using url, can get file and its me data from firebase storage
    val ref = FirebaseStorage.getInstance().getReferenceFromUrl(novelUrl)
    ref.metadata.addOnSuccessListener { storageMetadata ->
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

fun Fragment.showMessage(message: String) {
    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
}

fun isCustomer(type: String) = type == UserType.USER.name

fun View.hide(){ visibility = View.GONE }

fun View.show() {visibility = View.VISIBLE}