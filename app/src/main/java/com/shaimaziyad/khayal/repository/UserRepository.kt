package com.shaimaziyad.khayal.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.remote.DataBase
import kotlinx.coroutines.async
import com.shaimaziyad.khayal.utils.Result
import com.shaimaziyad.khayal.utils.Result.Error
import com.shaimaziyad.khayal.utils.Result.Success
import com.shaimaziyad.khayal.utils.SharePrefManager

import kotlinx.coroutines.supervisorScope

class UserRepository() {


    companion object {
        private const val TAG = "UserRepository"
    }


    private val remote = DataBase()

     suspend fun signUpWithEmail(user: User): Result<Boolean> {
        return supervisorScope {
            try {
                val signUpTask  = async {
                    val userId = remote.createUserAccount(user).user?.uid!!
                    user.uid = userId
                    remote.addUser(user)
                }
                val sendVerify = async { remote.setEmailVerify() }
                signUpTask.await()
                sendVerify.await()
                Success(true)
            }
            catch (ex: FirebaseAuthUserCollisionException){
                val message = "email is already taken"
                Log.d(TAG, message)
                Error(Exception(message))
            }
            catch (ex: FirebaseNetworkException) {
                val message = "network connection required"
                Log.d(TAG, message)
                Error(Exception(message))
            }
        }
    }



    suspend fun loginWithEmail(email: String, password: String, isRemOn: Boolean, context: Context): Result<Boolean> {
        return supervisorScope {
            try {
                val loginTask = async {
                    val userId = remote.signWithEmailAndPassword(email,password)?.user!!.uid
                    val user = remote.getUserById(userId)!!
                    val sharePref = SharePrefManager(context)
                    sharePref.saveUser(user,isRemOn)
                }
                loginTask.await()
                Success(true)
            }catch (ex: FirebaseAuthInvalidUserException) {
                val message = "user is not exist"
                Log.d(TAG, message)
                Error(Exception(message))
            }catch (ex: FirebaseAuthInvalidCredentialsException) {
                val message = "incorrect password"
                Log.d(TAG, message)
                Error(Exception(message))

            }catch (ex: FirebaseNetworkException) {
                val message = "network connection required"
                Log.d(TAG, message)
                Error(Exception(message))

            }
        }
    }


//    suspend fun loginWithEmail(email: String, password: String,isRemOn: Boolean): Result<Boolean> {
//        return supervisorScope {
//            try {
////                val signOutPref = async { sharePref.signOut() }
//                val loginTask = async {
//                    val userId = remote.signWithEmailAndPassword(email,password)?.user?.uid
//                    if (userId != null){
//                        val user = loadUser(userId) // todo: save the user in share pref
//                        if (isRemOn){
//                            // todo: keep user login
//                        }else{ }
//                    }
//                }
//                loginTask.await()
//                Success(true)
//            }catch (ex: FirebaseAuthInvalidUserException) {
//                val message = "user is not exist"
//                Log.d(TAG, message)
//                Error(Exception(message))
//            }catch (ex: FirebaseAuthInvalidCredentialsException) {
//                val message = "incorrect password"
//                Log.d(TAG, message)
//                Error(Exception(message))
//            }catch (ex: FirebaseNetworkException) {
//                val message = "network connection required"
//                Log.d(TAG, message)
//                Error(Exception(message))
//
//            }
//        }
//    }



    suspend fun signOut() = remote.signOut()

    suspend fun resetPassword(email: String,
                              onSuccess:( Task<Void>)-> Unit,
                              onError:(String)-> Unit) = remote.resetPassword(email, onSuccess, onError)

    suspend fun loadUser(userId: String) = remote.getUserById(userId)!!

}