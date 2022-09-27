package com.shaimaziyad.khayal.utils

import android.util.Log
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import java.util.*


enum class UserType { USER, ADMIN }

enum class DataStatus { LOADING, ERROR, SUCCESS }

fun getCurrentTime(): Date = Calendar.getInstance().time