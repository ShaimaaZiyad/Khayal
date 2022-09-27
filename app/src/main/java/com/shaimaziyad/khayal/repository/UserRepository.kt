//package com.shaimaziyad.khayal.repository
//
//import android.util.Log
//import com.google.android.gms.tasks.Task
//import com.google.firebase.FirebaseNetworkException
//import com.google.firebase.auth.AuthResult
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.FirebaseAuthInvalidUserException
//import com.shaimaziyad.khayal.data.User
//import com.shaimaziyad.khayal.remote.DataBase
//import kotlinx.coroutines.async
//import com.shaimaziyad.khayal.utils.Result.Success
//import com.shaimaziyad.khayal.utils.Result.Error
//import com.shaimaziyad.khayal.utils.Result
//
//import kotlinx.coroutines.supervisorScope
//import java.util.*
//
//class UserRepository() {
//
//    private val remote = DataBase()
//
//    suspend fun signUpWithEmail(user: User, onSuccess: (Task<AuthResult>) -> Unit, onError: (Exception) -> Unit) = remote.signUpWithEmail(user, onSuccess, onError)
//
////    suspend fun loginWithEmail( email: String, password: String, onSuccess: (Task<AuthResult>) -> Unit, onError: (Exception) -> Unit) = remote.login(email, password, onSuccess, onError)
////
//
//    suspend fun loginWithEmail(email: String, password: String,isRemOn: Boolean): Result<Boolean> {
//        return supervisorScope {
//            try {
////                val signOutPref = async { sharePref.signOut() }
//                val loginTask = async {
//                    val userId = remote.signWithEmailAndPassword(email,password)?.user!!.uid
//                    val user = remote.getUser(userId)!!
//                    createUserLogging(user,isRemOn)
//                }
//
//                signOutPref.await()
//
//                loginTask.await()
//
//                com.shaimaziyad.khayal.utils.Result.Success(true)
//            }catch (ex: FirebaseAuthInvalidUserException) {
//                val message = "user is not exist"
//                Log.d(TAG, message)
//                Result.Error(Exception(message))
//            }catch (ex: FirebaseAuthInvalidCredentialsException) {
//                val message = "incorrect password"
//                Log.d(TAG, message)
//                Result.Error(Exception(message))
//
//            }catch (ex: FirebaseNetworkException) {
//                val message = "network connection required"
//                Log.d(TAG, message)
//                Result.Error(Exception(message))
//
//            }
//        }
//    }
//
//
//
//    suspend fun signOut() = remote.signOut()
//
//    suspend fun resetPassword(email: String,
//                              onSuccess:( Task<Void>)-> Unit,
//                              onError:(String)-> Unit) = remote.resetPassword(email, onSuccess, onError)
//
//    suspend fun loadUser(userId: String) = remote.getUser(userId)
//
//}