package com.shaimaziyad.khayal.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.remote.DataBase

class UserRepository() {

    private val remote = DataBase()

    suspend fun signUpWithEmail(user: User, onSuccess: (Task<AuthResult>) -> Unit, onError: (Exception) -> Unit) = remote.signUpWithEmail(user, onSuccess, onError)

    suspend fun loginWithEmail( email: String, password: String, onSuccess: (Task<AuthResult>) -> Unit, onError: (Exception) -> Unit) = remote.login(email, password, onSuccess, onError)

    suspend fun signOut() = remote.signOut()

    suspend fun resetPassword(email: String,
                              onSuccess:( Task<Void>)-> Unit,
                              onError:(String)-> Unit) = remote.resetPassword(email, onSuccess, onError)

    suspend fun loadUser(userId: String) = remote.getUser(userId)

}