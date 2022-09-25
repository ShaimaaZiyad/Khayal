package com.shaimaziyad.khayal.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.PdfData
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.utils.Constants
import com.shaimaziyad.khayal.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.tasks.await

class DataBase {
    companion object{
        private const val TAG = "RemoteDataBase"

        private const val USERS_COLLECTION = "Users"

        private const val NOVELS_COLLECTION = "Novels"
        private const val PDFS_COUNT = "pdfsCount"
        private const val PDFS_FILED = "pdfs"
    }

    private val storage = Firebase.storage
    private val auth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    private val usersPath = fireStore.collection(USERS_COLLECTION)
    private val novelsPath = fireStore.collection(NOVELS_COLLECTION)

    // you can get the live data from here once the class is init
    private val _observeUsers = MutableLiveData<List<User>?>()
    val users: LiveData<List<User>?> = _observeUsers

    private val _observeNovels = MutableLiveData<List<NovelData>?>()
    val novels: MutableLiveData<List<NovelData>?> = _observeNovels

    init {
        /** get the live data of folders data from firebase **/
        observeUsers ()
        observeNovels ()
    }


    private fun observeUsers() {
        usersPath.addSnapshotListener { value, error ->
            if (error == null){
                if (value != null){
                    val userData = value.toObjects(User::class.java)
                    _observeUsers.value = userData
                }
            }
        }
    }

    suspend fun register(user: User, onSuccess:(Task<AuthResult>)-> Unit, onError:(Exception)-> Unit) {

        return supervisorScope {

            val task = async {
                auth.createUserWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            it.result.user?.sendEmailVerification()?.addOnCompleteListener { sendVerifyTask ->
                                Log.d(TAG,"Registration Success: ${it.result}")
                                onSuccess(it)
                                addUser(user)
                            }
                        }
                    }
                    .addOnFailureListener {
                        onError(it)
                        Log.d(TAG,"Registration Failed: message: ${it.message}" )
                    }
            }
            try {
                task.await()
            }catch (ex: Exception){
                onError(ex)
                Log.d(TAG,"Registration Failed: message: ${ex.message}" )
            }
        }

//        try {
//            auth.createUserWithEmailAndPassword(user.email,user.password)
//                .addOnSuccessListener { auth->
//                    val userId = auth.user?.uid!! // generic from firebase auth
//                    user.uid = userId
//                    addUser(user).addOnSuccessListener {
//                        onSuccess(auth)
//                    }
//                }
//                .addOnFailureListener {
//                    onError(it)
//                }
//        }catch (ex: Exception){
//            onError(ex)
//        }

    }

    private fun addUser(user: User) {
        try {
            usersPath.document(user.uid).set(user)
                .addOnCompleteListener {
                    Log.d(TAG,"adding User Success")
                }
                .addOnFailureListener {
                    Log.d(TAG,"adding User Failed: message: ${it.message} \n cause: ${it.cause}" )
                }
        } catch ( ex: Exception){
            Log.d(TAG,"Adding User Failed: message: ${ex.message} \n cause: ${ex.cause}" )
            Result.Error(ex)
        }
    }


    private suspend fun loginWithEmailAndPassword(email: String, password: String, onSuccess:(Task<AuthResult>)-> Unit, onError:(String)-> Unit) {
        return supervisorScope {
            val task = async {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            if (it.result.user!!.isEmailVerified){
                                Log.d(TAG,"Login Success: ${it.result}")
                                onSuccess(it)
                            }else{
                                val error = "email is not verified!"
                                Log.d(TAG,"Login Error: $error")
                                onError(error)
                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.d(TAG,"Login Error: ${it.message}")
                        onError(it.message.toString())
                    }
            }
            try {
                task.await()
            }catch (ex: Exception){
                Log.d(TAG,"Login Error: ${ex.message}")
                onError(ex.message.toString())
            }
        }
//        try {
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnSuccessListener {
//                    onSuccess(it)
//                }
//                .addOnFailureListener {
//                    onError(it)
//                }
//        }catch (ex: Exception){
//            onError(ex)
//        }
    }

    suspend fun login(email: String,
                       password: String,
                       onSuccess:(Task<AuthResult>)-> Unit,
                       onError:(String)-> Unit) =
        loginWithEmailAndPassword(email, password,onSuccess, onError)

    suspend fun getUser(userId: String) = usersPath.document(userId).get().await().toObject(User::class.java)

    suspend fun signOut(): Result<Boolean> {
        return supervisorScope {
            val remoteRes = async { auth.signOut() }
            try {
                remoteRes.await()
                Result.Success(true)
            }catch (ex: Exception){
                Result.Error(ex)
            }
        }
    }

    suspend fun resetPassword(email: String,
                              onSuccess:( Task<Void>)-> Unit,
                              onError:(String)-> Unit) {
        return supervisorScope {
            val remoteRes = async { auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    Log.d(TAG,"onResetPassword: Email is successfully sent")
                    onSuccess(it)
                }
                .addOnFailureListener {
                    Log.d(TAG,"onResetPassword: Failed to sent email: cause -> ${it.message}")
                    onError(it.message.toString())
                }
            }
            try {
                remoteRes.await()
            }catch (ex: Exception){
                Log.d(TAG,"onResetPassword: Failed to sent email: cause -> ${ex.message}")
            }
        }
    }

    private fun observeNovels() {
        novelsPath.addSnapshotListener { value, error ->
            if (error == null){
                if (value != null){
                    val novelData = value.toObjects(NovelData::class.java)
                    _observeNovels.value = novelData
                }
            }
        }
    }

    suspend fun getNovels():List<NovelData> = novelsPath.get().await().toObjects(NovelData::class.java)

    suspend fun addNovel(data : NovelData) = novelsPath.document(data.novelId).set(data)

    suspend fun updateNovel(data: NovelData) = novelsPath.document(data.novelId).update(data.toHashMap())


    // TODO: delete the pdf files before delete the novel data.
    suspend fun deleteNovel(novelData: NovelData)  = novelsPath.document(novelData.novelId).delete()

    suspend fun getNovelById(id: String): NovelData? {
        val ref = novelsPath.document(id).get().await()
        return ref.toObject(NovelData::class.java)
    }

    suspend fun addPdf(data: PdfData) = novelsPath.document(data.novelId).update(PDFS_FILED, FieldValue.arrayUnion(data)).addOnSuccessListener {
                // update pdf count
                novelsPath.document(data.novelId).get().addOnSuccessListener {
                    val pdfsCount = it.toObject(NovelData::class.java)?.pdfsCount!!
                    novelsPath.document(data.novelId).update(PDFS_COUNT,pdfsCount + 1)
                }
            }

    suspend fun deletePdf(pdfData: PdfData) {
        val ref = novelsPath.document(pdfData.novelId).get().await()
        if (ref != null){
            val folder = ref.toObject(NovelData::class.java)

            // delete pdf
            novelsPath.document(pdfData.novelId)
                .update(PDFS_FILED, FieldValue.arrayRemove(pdfData))

            // update total pdfs count in folder
            if (folder != null){
                folder.pdfsCount.plus(-1)
                updateNovel(folder)
            }

        }
    }



    // you can upload file or image (category could be image or file)
    suspend fun uploadFile(uri: Uri, fileName: String, fileType: String): Uri? {
        val filePath = storage.reference.child("$fileType/$fileName")
        val uploadTask = filePath.putFile(uri)
        val uriRef = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            filePath.downloadUrl
        }
        return uriRef.await()
    }


    suspend fun getPdf( pdfUrl : String) =
        storage.getReferenceFromUrl(pdfUrl).getBytes(Constants.MAX_BYTES_PDF)
}