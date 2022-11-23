package com.shaimaziyad.khayal.notification

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class NotifyService() : FirebaseMessagingService() {

    private companion object {
        private const val TAG = "NotifyService"
    }

    init {
        Log.i(TAG, "new notification")
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        Log.i(TAG, "onNewToken: $newToken")

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.i(TAG, "onMessageReceived: $message")

        val notificationManager = ContextCompat
            .getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager


        // you can set the notifications type here as you want

        notificationManager.createNotification(message, applicationContext)


    }
}