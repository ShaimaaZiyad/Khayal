package com.shaimaziyad.khayal.screens.notifications

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.shaimaziyad.khayal.data.Notification
import com.shaimaziyad.khayal.repository.NotifyRepository
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.utils.*
import kotlinx.coroutines.launch

class NotificationsViewModel(private val notifyRepo: NotifyRepository,
                             private val userRepo: UserRepository) : ViewModel() {

    companion object{
        const val TAG = "NotificationViewModel"
    }


    private val _notifications = MutableLiveData<List<Notification>?>()
    val notifications: LiveData<List<Notification>?> = _notifications


    private val _filtered = MutableLiveData<List<Notification>?>()
    val filtered: LiveData<List<Notification>?> = _filtered

    private val _tap = MutableLiveData<SelectedSection>(SelectedSection.UnRead)
    val tap: LiveData<SelectedSection> = _tap



    private val _reads = MutableLiveData<List<Notification>?>()
    val reads: LiveData<List<Notification>?> = _reads

    private val _unRead = MutableLiveData<List<Notification>?>()
    val unRead: LiveData<List<Notification>?> = _unRead


    private val _system = MutableLiveData<List<Notification>?>()
    val system: LiveData<List<Notification>?> = _system


    private val _notifyStatus = MutableLiveData<DataStatus?>()
    val notifyStatus: LiveData<DataStatus?> = _notifyStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error



    fun loadNotificationsByUserId() {
        Log.d(TAG,"onLoading.. Loading notifications")
        resetStatus()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        _notifyStatus.value = DataStatus.LOADING
        viewModelScope.launch {
            val res = notifyRepo.loadNotifications()
            if (res is Result.Success) {
                Log.d(TAG,"onSuccess: user notifications size: ${res.data.size}")
                _notifyStatus.value = DataStatus.SUCCESS
                val data = res.data
                _notifications.value = data.sortedBy { it.createDate }.reversed() ?: emptyList()
                setUnReadsNotify(userId)
                setReadNotify(userId)
                setSystemNotify()
                resetStatus()
            }
            else if(res is Result.Error) {
                Log.d(TAG,"onError: error happen during fetching user notifications due to ${res.exception.message}")
                _notifyStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
                resetStatus()

            }

        }
    }


    private fun setUnReadsNotify(userId: String) {
        Log.d(TAG,"111 onSuccess: user notifications size: ${_notifications.value?.size}")
        _unRead.value = _notifications.value?.filter { it.type == NotifyType.Direct.name && it.isRead == false && it.targetUser == userId }
        Log.d(TAG,"unRead: ${_unRead.value?.size}")
    }



    private fun setReadNotify(userId: String) {
        Log.d(TAG,"222 onSuccess: user notifications size: ${_notifications.value?.size}")
        _reads.value = _notifications.value?.filter { it.type == NotifyType.Direct.name && it.isRead == true && it.targetUser == userId }
        Log.d(TAG,"read: ${_reads.value?.size}")
    }

    private fun setSystemNotify() {
        Log.d(TAG,"333 onSuccess: user notifications size: ${_notifications.value?.size}")
        _system.value = _notifications.value?.filter { it.type == NotifyType.System.name }
        Log.d(TAG,"system: ${_system.value?.size}")
    }


    fun filter(type: String,isRead: Boolean?): List<Notification> {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            if (type == NotifyType.Direct.name) {
                _filtered.value = if (isRead == true) {
                    _tap.value = SelectedSection.Read
                    _notifications.value?.filter { it.type == NotifyType.Direct.name && it.isRead == true && it.targetUser == userId }
                }else{
                    _tap.value = SelectedSection.UnRead
                    _notifications.value?.filter { it.type == NotifyType.Direct.name && it.isRead == false && it.targetUser == userId }
                }
            }
            if (type == NotifyType.System.name) {
                _tap.value = SelectedSection.System
                _filtered.value = _notifications.value?.filter { it.type == NotifyType.System.name }
            }

        }
        return _filtered.value?.sortedBy { it.createDate }?.reversed() ?: emptyList()
    }




    fun pushNotify(notify: Notification) {
        resetStatus()
        _notifyStatus.value = DataStatus.LOADING
        Log.d(TAG,"onPushNotify: Loading...")
        viewModelScope.launch {
            val res = notifyRepo.pushNotify(notify)
            if (res is Result.Success) {
                Log.d(TAG,"onPushNotify: push notify have been success...")
                _notifyStatus.value = DataStatus.SUCCESS
                resetStatus()

            }else if( res is Result.Error){
                Log.d(TAG,"onPushNotify: push notify Failed due to ${res.exception.message}")
                _notifyStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
                resetStatus()
            }
        }
    }


    fun updateNotify(notify: Notification) {
        resetStatus()
        _notifyStatus.value = DataStatus.LOADING
        Log.d(TAG,"onUpdateNotify: Loading...")
        viewModelScope.launch {
            val res = notifyRepo.updateNotify(notify)
            if (res is Result.Success) {
                Log.d(TAG,"onUpdateNotify: update notify have been success...")
                _notifyStatus.value = DataStatus.SUCCESS
            }else if( res is Result.Error){
                Log.d(TAG,"onUpdateNotify: update notify Failed due to ${res.exception.message}")
                _notifyStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }


    fun removeNotify(notify: Notification) {
        resetStatus()
        _notifyStatus.value = DataStatus.LOADING
        Log.d(TAG,"onRemoveNotify: Loading...")
        viewModelScope.launch {
            val res = notifyRepo.removeNotify(notify)
            if (res is Result.Success) {
                Log.d(TAG,"onRemoveNotify: remove notify have been success...")
                _notifyStatus.value = DataStatus.SUCCESS

            }else if( res is Result.Error){
                Log.d(TAG,"onRemoveNotify: remove notify Failed due to ${res.exception.message}")
                _notifyStatus.value = DataStatus.ERROR
                _error.value = res.exception.message
            }
        }
    }


    fun resetStatus() {
        _notifyStatus.value = null
        _error.value = null
    }


}