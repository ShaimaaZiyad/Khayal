package com.shaimaziyad.khayal

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.shaimaziyad.khayal.data.User

class SharePrefManager (context: Context) {
    var userSession: SharedPreferences = context.getSharedPreferences("userSessionData", Context.MODE_PRIVATE)
    private val editor = userSession.edit()
    fun saveUser(user: User,isRemOn: Boolean){
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(KEY_USER,json)
        editor.putBoolean(KEY_REMEMBER_ME, isRemOn)
        editor.commit()
    }
    fun loadUser(): User {
        val gson = Gson()
        val json = userSession.getString(KEY_USER, "")
        return gson.fromJson(json, User::class.java)
    }
    fun createLoginSession (
        id: String,
        isRemOn: Boolean,
        userType: String,
    ) {
        editor.putString(KEY_ID, id)
        editor.putString(KEY_USER_TYPE, userType)
        editor.putBoolean(KEY_REMEMBER_ME, isRemOn)
        editor.commit()
    }
    fun isRememberMeOn(): Boolean = userSession.getBoolean(KEY_REMEMBER_ME, false)
    fun getName(): String? = userSession.getString(KEY_NAME, "")
    fun getUserType(): String? = userSession.getString(KEY_USER_TYPE, "")
    fun getUserDataFromSession(): HashMap<String, String?> {
        return hashMapOf(
            KEY_ID to userSession.getString(KEY_ID, null),
            KEY_NAME to userSession.getString(KEY_NAME, null),
        )
    }
    fun getUserIdFromSession(): String? = userSession.getString(KEY_ID, "")
    fun signOut() {
        editor.clear()
        editor.commit()
    }
    companion object {
        private const val IS_LOGIN = "isLoggedIn"
        private const val KEY_NAME = "userName"
        private const val KEY_ID = "userId"
        private const val KEY_USER_TYPE = "userType"
        private const val KEY_REMEMBER_ME = "isRemOn"
        private const val KEY_USER = "User"
    }
}