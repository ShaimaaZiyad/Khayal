package com.shaimaziyad.khayal.utils


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.shaimaziyad.khayal.data.User


class SharePrefManager(context: Context) {

    private val userSession = context.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)
    private val editor = userSession.edit()

    fun saveUser(user: User,isRemOn: Boolean) {
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

    fun isLogged() = userSession.getBoolean(KEY_REMEMBER_ME,false)


    fun signOut() {
        editor.clear()
        editor.commit()
    }

    companion object {
        private const val KEY_REMEMBER_ME = "isRemOn"
        private const val KEY_USER = "User"
    }

}

