package com.shaimaziyad.khayal.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.notification.getToken
import com.shaimaziyad.khayal.remote.DataBase
import com.shaimaziyad.khayal.utils.FileType
import kotlinx.coroutines.async
import com.shaimaziyad.khayal.utils.Result
import com.shaimaziyad.khayal.utils.Result.Error
import com.shaimaziyad.khayal.utils.Result.Success
import com.shaimaziyad.khayal.utils.SharePrefManager
import kotlinx.coroutines.delay

import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.tasks.await

class UserRepository(private val remote: DataBase,
                     private val sharePref: SharePrefManager) {

    companion object {
        private const val TAG = "UserRepository"
    }


    val isLogged = sharePref.isLogged()
    val user = sharePref.loadUser()

//    private val remote = DataBase()

    suspend fun update(user: User): Result<Boolean> {
        return supervisorScope {
            val task  = async {
                remote.updateUser(user)
//                sharePref.saveUser(user,null)
            }
            try {
                task.await()
                Success(true)
            }catch (ex: Exception) {
                Error(ex)
            }
        }
    }

    suspend fun uploadProfile(uri: Uri, fileName: String) = remote.uploadFile(uri,fileName, FileType.IMAGE_PROFILE.name)


    suspend fun signUpWithEmail(user: User): Result<Boolean> {
        return supervisorScope {
            val signUpTask = async {
                val newUser = remote.createUserAccount(user).await().user!!
                val userId = newUser.uid
                user.uid = userId
                remote.addUser(user)
                newUser.sendEmailVerification()
            }
            try {
                signUpTask.await()
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




    suspend fun loginWithEmail(email: String, password: String, isRemOn: Boolean, context: Context): Result<User> {
        var mUser: User? = null
        return supervisorScope {
            val loginTask = async {
                val loginUser = remote.signWithEmailAndPassword(email,password)?.user
                val userId = loginUser?.uid
                if (userId != null){
                    val user = remote.getUserById(userId)
                    mUser = user
                    getToken { newToken -> user.token = newToken }
                    val sharePref = SharePrefManager(context)
                    sharePref.saveUser(user,isRemOn)
                    Log.d(TAG,"user name: ${user.name}")
                    async {
                        delay(200)
                        remote.updateUser(user)
                    }

                }

            }
            try {
                loginTask.await()
                Success(mUser!!)
//                if (isVerified == true) Success(true) else Error(Exception("Email is not verified")) // we will use this after verification function


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


    suspend fun signOut() : Result<Boolean> {
        return supervisorScope {
            val remoteSignOut = async { remote.signOut() }
            val localSignOut = async { sharePref.signOut() }
            try {
                remoteSignOut.await()
                localSignOut.await()
                Success(true)
            }catch (ex: Exception){
                Error(ex)
            }
        }
    }


    suspend fun resetPassword(email: String): Result<Boolean> {
        return supervisorScope {
            val task = async { remote.resetPassword(email) }
            try {
                task.await()
                Success(true)
            }catch (ex: Exception){
                Error(ex)
            }
        }
    }


//    suspend fun loadUser(userId: String) = remote.getUserById(userId)!!

    suspend fun loadUser(userId: String) = remote.getUserById(userId)


    suspend fun loadUsers(): Result<List<User>> {
        return supervisorScope {
            val loadTask = async { remote.getUsers() }
            try {
                Success(loadTask.await())
            }catch (ex: Exception){
                Error(ex)
            }
        }
    }



}