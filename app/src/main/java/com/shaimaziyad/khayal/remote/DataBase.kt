package com.shaimaziyad.khayal.remote

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.utils.Constants
import kotlinx.coroutines.tasks.await

//@RequiresApi(Build.VERSION_CODES.P)
class DataBase {
    companion object{
        private const val TAG = "RemoteDataBase"

        private const val USERS_COLLECTION = "Users"

        private const val CATEGORIES_COLLECTION = "Categories"
        private const val NOVELS_COUNT = "novelsCount"
        private const val NOVELS_FILED = "novels"

        private const val NOVELS_COLLECTION = "Novels"
        private const val PDFS_COUNT = "pdfsCount"
        private const val PDFS_FILED = "pdfs"
    }

    private val storage = Firebase.storage
    private val auth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()
    private val usersPath = fireStore.collection(USERS_COLLECTION)
//    private val categoriesPath = fireStore.collection(CATEGORIES_COLLECTION)
    private val novelsPath = fireStore.collection(NOVELS_COLLECTION)

    // you can get the live data from here once the class is init
    private val _observeUsers = MutableLiveData<List<User>?>()
    val users: LiveData<List<User>?> = _observeUsers

//    private val _observeCategories = MutableLiveData<List<CategoryData>?>()
//    val categories: MutableLiveData<List<CategoryData>?> = _observeCategories

    private val _observeNovels = MutableLiveData<List<NovelData>?>()
    val novels: MutableLiveData<List<NovelData>?> = _observeNovels

    init {
        /** get the live data of folders data from firebase **/
        observeUsers ()
//        observeCategories ()
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

    private fun addUser(user: User) = usersPath.add(user)

    suspend fun register(user: User, onSuccess:(AuthResult)-> Unit, onError:(Exception)-> Unit) {
        try {
            auth.createUserWithEmailAndPassword(user.email,user.password)
                .addOnSuccessListener { auth->
                    val userId = auth.user?.uid!! // generic from firebase auth
                    user.uid = userId
                    addUser(user).addOnSuccessListener {
                        onSuccess(auth)
                    }
                }
                .addOnFailureListener {
                    onError(it)
                }
        }catch (ex: Exception){
            onError(ex)
        }

    }

    suspend fun getUser(userId: String) = usersPath.document(userId).get().await().toObject(User::class.java)

    suspend fun login(email: String, password: String, onSuccess:(AuthResult)-> Unit, onError:(Exception)-> Unit) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    onSuccess(it)
                }
                .addOnFailureListener {
                    onError(it)
                }
        }catch (ex: Exception){
            onError(ex)
        }
    }


//    private fun observeCategories() {
//        categoriesPath.addSnapshotListener { value, error ->
//            if (error == null){
//                if (value != null){
//                    val categoryData = value.toObjects(CategoryData::class.java)
//                    _observeCategories.value = categoryData
//                }
//            }
//        }
//    }


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


//    suspend fun getCategories():List<CategoryData> = categoriesPath.get().await().toObjects(CategoryData::class.java)

//    suspend fun addCategory(data: CategoryData) = categoriesPath.document(data.categoryId).set(data)

//    suspend fun updateCategory(data: CategoryData) = categoriesPath.document(data.categoryId).update(data.toHashMap())

//    suspend fun deleteCategory(data: CategoryData) = categoriesPath.document(data.categoryId).delete()

//    suspend fun getCategoryById(id: String): CategoryData? {
//        val ref = categoriesPath.document(id).get().await()
//        return ref.toObject(CategoryData::class.java)
//    }

    suspend fun getNovels():List<NovelData> = novelsPath.get().await().toObjects(NovelData::class.java)

    suspend fun addNovel(data : NovelData) = novelsPath.document(data.novelId).set(data)



//    suspend fun addNovel(data : NovelData) =
//        categoriesPath.document(data.categoryId)
//            .update(NOVELS_FILED,FieldValue.arrayUnion(data)).addOnSuccessListener {
//                // update novel count
//                categoriesPath.document(data.categoryId).get().addOnSuccessListener {
//                    val novelsCount = it.toObject(CategoryData::class.java)?.novelsCount!!
//                    categoriesPath.document(data.categoryId).update(NOVELS_COUNT, novelsCount + 1)
//                }
//            }


    suspend fun updateNovel(data: NovelData) = novelsPath.document(data.novelId).update(data.toHashMap())


    /// TODO: delete the pdf files before delete the novel data.
    suspend fun deleteNovel(novelData: NovelData)  = novelsPath.document(novelData.novelId).delete()

    suspend fun getNovelById(id: String): NovelData? {
        val ref = novelsPath.document(id).get().await()
        return ref.toObject(NovelData::class.java)
    }

//    suspend fun deleteNovel(novelData: NovelData) {
//        val ref = categoriesPath.document(novelData.categoryId).get().await()
//        if (ref != null){
//            val category = ref.toObject(CategoryData::class.java)
//
//            // delete novel
//            categoriesPath.document(novelData.categoryId)
//                .update(NOVELS_FILED, FieldValue.arrayRemove(novelData))
//
//            // update total novels count in folder
//            if (category != null){
//                category.novelsCount.plus(-1)
//                updateCategory(category)
//            }
//
//        }
//    }



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