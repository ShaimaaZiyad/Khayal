package com.shaimaziyad.khayal.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.HttpException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.utils.Constants
import com.shaimaziyad.khayal.utils.ERR_UPLOAD
import com.shaimaziyad.khayal.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.tasks.await
import java.io.FileNotFoundException
import java.util.*

class DataBase {

    companion object{
        private const val TAG = "RemoteDataBase"

        private const val USERS_COLLECTION = "Users"

        private const val FIELD_EMAIL = "email"
        private const val NOVELS_COLLECTION = "Novels"
        private const val PDFS_COUNT = "pdfsCount"
        private const val PDFS_FILED = "pdfs"
    }

    private val storage = Firebase.storage
    private val auth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    private val usersPath = fireStore.collection(USERS_COLLECTION)
    private val novelsPath = fireStore.collection(NOVELS_COLLECTION)

//    private val userId = auth.currentUser?.uid!! // use this variable only after user login to firebase.

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

    suspend fun isUserExist(email: String) =  usersPath.whereEqualTo(FIELD_EMAIL, email).get().await().first().exists()

    suspend fun signWithEmailAndPassword(email: String, password: String): AuthResult? {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun createUserAccount(user: User) = auth.createUserWithEmailAndPassword(user.email,user.password).await()


    fun addUser(user: User) = usersPath.document(user.uid).set(user)

    suspend fun setEmailVerify() = auth.currentUser?.sendEmailVerification()

    suspend fun getUserById(userId: String) = usersPath.document(userId).get().await().toObject(User::class.java)


    suspend fun getUser() {
        usersPath.get().await()
    }

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

    suspend fun addNovel(data : NovelData) = novelsPath.document(data.novelId).set(data).await()

    suspend fun updateNovel(data: NovelData) = novelsPath.document(data.novelId).update(data.toHashMap())


    // TODO: delete the pdf files before delete the novel data.
    suspend fun deleteNovel(novelData: NovelData)  = novelsPath.document(novelData.novelId).delete()


    suspend fun getNovelById(id: String): NovelData? {
        val ref = novelsPath.document(id).get().await()
        return ref.toObject(NovelData::class.java)
    }


    suspend fun addPdf(novelData: NovelData,pdfId: String) = novelsPath.document(pdfId).update(PDFS_FILED, FieldValue.arrayUnion(pdfId)).addOnSuccessListener {
                // update pdf count
                novelsPath.document(pdfId).get().addOnSuccessListener {
                    val pdfsCount = it.toObject(NovelData::class.java)?.pdfsCount!!
                    novelsPath.document(pdfId).update(PDFS_COUNT,pdfsCount + 1)
                }
            }

    suspend fun deletePdf(pdfId: String) {
        val ref = novelsPath.document(pdfId).get().await()
        if (ref != null){
            val folder = ref.toObject(NovelData::class.java)

            // delete pdf
            novelsPath.document(pdfId)
                .update(PDFS_FILED, FieldValue.arrayRemove(pdfId))

            // update total pdfs count in folder
            if (folder != null) {
                folder.pdfsCount.plus(-1)
                updateNovel(folder)
            }

        }
    }


    // you can upload file or image (category could be image or file)
    suspend fun uploadFile(uri: Uri, fileName: String, fileType: String): String {
        val filePath = storage.reference.child("$fileType/$fileName")
        return if (uri.toString().contains("content")) { // upload new file
            val uriRef = filePath.putFile(uri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                filePath.downloadUrl
            }
            uriRef.await().toString()
        }else{ // keep the old file as it is
            uri.toString()
        }
    }




    suspend fun insertFiles(filesUri: List<Uri>,fileType: String): List<String> {
        var urlList = mutableListOf<String>()
        filesUri.forEach label@{ uri ->
            val uniId = UUID.randomUUID().toString()
            val fileName = uniId + uri.lastPathSegment?.split("/")?.last()
            try {
                val downloadUrl = uploadFile(uri, fileName,fileType)
                urlList.add(downloadUrl)
            } catch (e: Exception) {
                Log.d(TAG, "Upload file error due to: $e")
//                revertUpload(fileName)
                urlList = mutableListOf()
                urlList.add(ERR_UPLOAD)
                return@label
            }
        }
        return urlList
    }


    suspend fun getPdf( pdfUrl : String) =
        storage.getReferenceFromUrl(pdfUrl).getBytes(Constants.MAX_BYTES_PDF)
}