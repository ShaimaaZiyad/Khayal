package com.shaimaziyad.khayal1.repository

import android.net.Uri
import com.google.firebase.FirebaseNetworkException
import com.shaimaziyad.khayal1.data.Novel
import com.shaimaziyad.khayal1.remote.DataBase
import com.shaimaziyad.khayal1.utils.FileType
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import com.shaimaziyad.khayal1.utils.Result.Success
import com.shaimaziyad.khayal1.utils.Result.Error
import com.shaimaziyad.khayal1.utils.Result

class NovelRepository(private val remote: DataBase) {

    suspend fun addNovel(novel: Novel): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.addNovel(novel) }
            try {
                addTask.await()
                Success(true)
            } catch (ex: Exception) {
                Error(ex)
            }
        }
    }

    suspend fun updateNovel(novel: Novel): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.updateNovel(novel) }
            try {
                addTask.await()
                Success(true)
            } catch (ex: Exception) {
                Error(ex)
            }
        }
    }


    // todo: we need to delete files before delete the data
    suspend fun deleteNovel(novel: Novel): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.deleteNovel(novel) }
            try {
                addTask.await()
                Success(true)
            } catch (ex: Exception) {
                Error(ex)
            }
        }
    }

    suspend fun uploadCover(uri: Uri, fileName: String) =
        remote.uploadFile(uri, fileName, FileType.IMAGE.name)

    suspend fun loadNovels(): Result<List<Novel>> {
        return supervisorScope {
            val loadTask = async { remote.getNovels() }
            try {
                Success(loadTask.await())
            }
            catch (ex: FirebaseNetworkException){
                Error(ex)
            }
            catch (ex: Exception) {
                Error(ex)
            }
        }
    }


}