package com.shaimaziyad.khayal.repository

import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.remote.DataBase
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import com.shaimaziyad.khayal.utils.Result.Success
import com.shaimaziyad.khayal.utils.Result.Error
import com.shaimaziyad.khayal.utils.Result

class NovelRepository {

    private val remote = DataBase()

    val novels = remote.novels

    suspend fun addNovel(novel: NovelData) = remote.addNovel(novel)

    suspend fun updateNovel(novel: NovelData) = remote.updateNovel(novel)

    suspend fun deleteNovel(novel: NovelData) = remote.deleteNovel(novel)


    suspend fun loadNovels(): Result<List<NovelData>> {
        return supervisorScope {
            val loadTask = async { remote.getNovels() }
            try {
                Success( loadTask.await())
            }catch (ex: Exception){
                Error(ex)
            }
        }
    }



}