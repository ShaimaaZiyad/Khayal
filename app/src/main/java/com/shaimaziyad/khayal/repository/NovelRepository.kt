package com.shaimaziyad.khayal.repository

import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.remote.DataBase

class NovelRepository {

    private val remote = DataBase()

    val novels = remote.novels


    suspend fun addNovel(novel: NovelData) = remote.addNovel(novel)

    suspend fun updateNovel(novel: NovelData) = remote.updateNovel(novel)

    suspend fun deleteNovel(novel: NovelData) = remote.deleteNovel(novel)

    suspend fun getNovels() = remote.getNovels()


}