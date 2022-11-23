package com.shaimaziyad.khayal.repository

import android.net.Uri
import com.shaimaziyad.khayal.data.Banner
import com.shaimaziyad.khayal.remote.DataBase
import com.shaimaziyad.khayal.utils.FileType
import com.shaimaziyad.khayal.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class BannerRepository(private val remote: DataBase) {


    suspend fun uploadBannerCover(uri: Uri, fileName: String) =
        remote.uploadFile(uri, fileName, FileType.IMAGE_BANNER.name)


    suspend fun loadBanners(): Result<List<Banner>> {
        return supervisorScope {
            val loadTask = async { remote.getBanners() }
            try {
                Result.Success(loadTask.await())
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }

    suspend fun submitBanner(banner: Banner): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.addBanner(banner) }
            try {
                addTask.await()
                Result.Success(true)
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }


    suspend fun updateBanner(banner: Banner): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.updateBanner(banner) }
            try {
                addTask.await()
                Result.Success(true)
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }


    suspend fun deleteBanner(banner: Banner): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.deleteBanner(banner) }
            try {
                addTask.await()
                Result.Success(true)
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }


}