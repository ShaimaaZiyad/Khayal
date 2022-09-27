package com.shaimaziyad.khayal.utils

import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage


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


fun View.hide(){ visibility = View.GONE }

fun View.show() {visibility = View.VISIBLE}