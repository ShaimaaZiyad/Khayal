package com.shaimaziyad.khayal1.repository

import com.shaimaziyad.khayal1.data.Notification
import com.shaimaziyad.khayal1.remote.DataBase
import com.shaimaziyad.khayal1.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope


class NotifyRepository(
    private val remote: DataBase,
    private val userRepo: UserRepository
) {


//    fun notifications() = remote.observeNotification


    suspend fun loadNotifications(): Result<List<Notification>> {
        return supervisorScope {
            val loadTask = async { remote.getNotifications() }
            try {
                Result.Success(loadTask.await())
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }

    suspend fun pushNotify(notify: Notification): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.addNotify(notify) }
            try {
                addTask.await()
                Result.Success(true)
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }


    suspend fun updateNotify(notify: Notification): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.updateNotify(notify) }
            try {
                addTask.await()
                Result.Success(true)
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }


    suspend fun removeNotify(notify: Notification): Result<Boolean> {
        return supervisorScope {
            val addTask = async { remote.deleteNotify(notify) }
            try {
                addTask.await()
                Result.Success(true)
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }


}