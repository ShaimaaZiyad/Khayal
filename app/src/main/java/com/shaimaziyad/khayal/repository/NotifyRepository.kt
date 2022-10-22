package com.shaimaziyad.khayal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shaimaziyad.khayal.data.Notification
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.remote.DataBase
import com.shaimaziyad.khayal.screens.notifications.Notifications
import com.shaimaziyad.khayal.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class NotifyRepository(
    private val remote: DataBase,
    private val userRepo: UserRepository
) {


//    private val user = userRepo.user
//    private val remote = DataBase()


    fun notifications() = remote.observeNotification


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
            val addTask = async { remote.removeNotify(notify) }
            try {
                addTask.await()
                Result.Success(true)
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }


}