package com.shaimaziyad.khayal.repository

import com.google.firebase.auth.AuthResult
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.remote.DataBase

class UserRepository() {

    private val remote = DataBase()

    suspend fun register(user: User, onSuccess:(AuthResult)-> Unit, onError:(Exception)-> Unit) =
        remote.register(user, onSuccess, onError)


    suspend fun login(email: String, password: String, onSuccess:(AuthResult)-> Unit, onError:(Exception)-> Unit) =
        remote.login(email, password, onSuccess, onError)


    suspend fun loadUser(userId: String) = remote.getUser(userId)

}