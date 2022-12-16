package com.shaimaziyad.khayal1.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shaimaziyad.khayal1.data.Banner
import com.shaimaziyad.khayal1.data.Notification
import com.shaimaziyad.khayal1.data.Novel
import com.shaimaziyad.khayal1.data.User
import com.shaimaziyad.khayal1.utils.Constants
import com.shaimaziyad.khayal1.utils.ERR_UPLOAD
import com.shaimaziyad.khayal1.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.tasks.await
import java.util.*

class DataBase() {

    companion object {
        private const val TAG = "RemoteDataBase"

        private const val USERS_COLLECTION = "Users"
        private const val NOTIFICATION_COLLECTION = "Notifications"
        private const val BANNERS_COLLECTIONS = "Banners"

        private const val FIELD_TARGET_USER = "targetUser"
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
    private val notificationsPath = fireStore.collection(NOTIFICATION_COLLECTION)
    private val bannersPath = fireStore.collection(BANNERS_COLLECTIONS)


    private val _observeUsers = MutableLiveData<List<User>?>()
    val users: LiveData<List<User>?> = _observeUsers

    private val _observeNovels = MutableLiveData<List<Novel>?>()
    val novels: MutableLiveData<List<Novel>?> = _observeNovels

    private val _observeNotification = MutableLiveData<List<Notification>?>()
    val observeNotification: MutableLiveData<List<Notification>?> = _observeNotification


    init {
        observeNotification()

        /** get the live data of folders data from firebase **/
//        observeUsers ()
//        observeNovels ()
    }


    private fun observeNotification() {
        notificationsPath.addSnapshotListener { value, error ->
            if (error == null) {
                if (value != null) {
                    val notifications = value.toObjects(Notification::class.java)
                    _observeNotification.value = notifications
                } else {
                    _observeNotification.value = emptyList()
                }
            } else {
                _observeNotification.value = emptyList()
            }
        }
    }


    suspend fun signWithEmailAndPassword(email: String, password: String): AuthResult? {
        return auth.signInWithEmailAndPassword(email, password).await()
    }


    suspend fun createUserAccount(user: User) =
        auth.createUserWithEmailAndPassword(user.email, user.password)


    fun addUser(user: User) = usersPath.document(user.uid).set(user)

    suspend fun getUserById() =
        usersPath.document(auth.currentUser?.uid!!).get().await().toObject(User::class.java)
            ?: User()

    suspend fun updateUser(user: User) = usersPath.document(user.uid).update(user.toHashMap())

    suspend fun getUsers(): List<User> =
        usersPath.get().await().toObjects(User::class.java).filter { it.uid != Constants.ADMIN_ID }

    suspend fun signOut(): Result<Boolean> {
        return supervisorScope {
            val remoteRes = async { auth.signOut() }
            try {
                remoteRes.await()
                Result.Success(true)
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }

    suspend fun resetPassword(email: String) = auth.sendPasswordResetEmail(email)

    private fun observeNovels() {
        novelsPath.addSnapshotListener { value, error ->
            if (error == null) {
                if (value != null) {
                    val novelData = value.toObjects(Novel::class.java)
                    _observeNovels.value = novelData
                }
            }
        }
    }


    suspend fun getBanners(): List<Banner> = bannersPath.get().await().toObjects(Banner::class.java)
    suspend fun addBanner(data: Banner) = bannersPath.document(data.id).set(data)
    suspend fun updateBanner(data: Banner) = bannersPath.document(data.id).update(data.toHashMap())
    suspend fun deleteBanner(data: Banner) = bannersPath.document(data.id).delete()


    suspend fun getNotifications(): List<Notification> =
        notificationsPath.get().await().toObjects(Notification::class.java)

    suspend fun addNotify(data: Notification) = notificationsPath.document(data.id).set(data)
    suspend fun updateNotify(data: Notification) =
        notificationsPath.document(data.id).update(data.toHashMap())

    suspend fun deleteNotify(data: Notification) = notificationsPath.document(data.id).delete()


    suspend fun getNovels(): List<Novel> = novelsPath.get().await().toObjects(Novel::class.java)
    suspend fun addNovel(data: Novel) = novelsPath.document(data.novelId).set(data)
    suspend fun updateNovel(data: Novel) =
        novelsPath.document(data.novelId).update(data.toHashMap())

    // TODO: delete the pdf files before delete the novel data.
    suspend fun deleteNovel(novel: Novel) = novelsPath.document(novel.novelId).delete()


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
        } else { // keep the old file as it is
            uri.toString()
        }
    }

    suspend fun loadPdf(url: String): ByteArray =
        storage.getReferenceFromUrl(url).getBytes(Constants.MAX_BYTES_PDF).await()

    suspend fun insertFiles(filesUri: List<Uri>, fileType: String): List<String> {
        var urlList = mutableListOf<String>()
        filesUri.forEach label@{ uri ->
            val uniId = UUID.randomUUID().toString()
            val fileName = uniId + uri.lastPathSegment?.split("/")?.last()
            try {
                val downloadUrl = uploadFile(uri, fileName, fileType)
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


    suspend fun getPdf(pdfUrl: String) =
        storage.getReferenceFromUrl(pdfUrl).getBytes(Constants.MAX_BYTES_PDF)
}