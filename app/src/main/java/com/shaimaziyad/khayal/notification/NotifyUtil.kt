package com.shaimaziyad.khayal.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.shaimaziyad.khayal.MainActivity
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Notification
import com.shaimaziyad.khayal.data.PushNotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


object Notify {

    const val BASE_URL = "https://fcm.googleapis.com" // url for fcm ..
    const val SERVER_KEY =
        "AAAA4G0_0OI:APA91bFDRzIQHZlo5rMZMzsFo8LkopjC8kqbSWmkD8EwXvqclCmIIR9srGBULpJGqUu6P906YaeJP07gXGzIoQthbKTVDyLhN9gV0Xo33zqQ0BK7v-JBhq0Wor3LlpTbXwxpvrLedwe3" // server key present in your app in firebase in setting ..
    const val CONTENT_TYPE = "application/json"

    const val CHANNEL_ID = "my_channel"
}

const val TAG = "NotifyUtil"

fun NotificationManager.createNotification(message: RemoteMessage, context: Context) {

    // notify data
    val id = message.data["id"]
    val title = message.data["title"]
    val body = message.data["body"]
    val icon = message.data["icon"]
    val internalLink = message.data["internalLink"]
    val externalLink = message.data["externalLink"]
    val targetUser = message.data["targetUser"]!!
    val createDate = message.data["createDate"]


    val notificationManager =
        getSystemService(context, NotificationManager::class.java) as NotificationManager
    val notificationID = Random.nextInt()

    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(externalLink)
    ) // here we set the intent you can use link or any thing ..
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


    val navDeepLinkIntent = NavDeepLinkBuilder(context)
    val pendingIntent = PendingIntent.getActivity(
        context.applicationContext,
        0,
        intent,
        PendingIntent.FLAG_ONE_SHOT
    )


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(notificationManager)
    }

    val notification = NotificationCompat.Builder(context, Notify.CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(body)
        .setSmallIcon(R.drawable.ic_app)
        .setAutoCancel(true)


    if (!externalLink.isNullOrEmpty()) { // it will move the user to any link by using browser eg: facebook, tweeter, blog etc..
        notification.setContentIntent(pendingIntent)
    }


    if (!internalLink.isNullOrEmpty()) { // it will move the user to any screen inside the application
        navDeepLinkIntent.setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(internalLink.toInt())
        notification.setContentIntent(navDeepLinkIntent.createPendingIntent()) // this code when click on notification..
    }

    if (internalLink.isNullOrEmpty() && externalLink.isNullOrEmpty()) { // it will move the user to default screen
        navDeepLinkIntent.setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(R.id.home) // default screen
        notification.setContentIntent(navDeepLinkIntent.createPendingIntent())
    }

    notificationManager.notify(notificationID, notification.build())
}


@RequiresApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(notificationManager: NotificationManager) {
    val channelName = "channelName"
    val channel = NotificationChannel(
        Notify.CHANNEL_ID,
        channelName,
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "My channel description"
        enableLights(true)
        lightColor = Color.GREEN
    }
    notificationManager.createNotificationChannel(channel) // create notification channel ..
}

fun sendNotification(notification: Notification, token: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = NotifyRetrofit.api.postNotification(notifyData(notification, token))
            Log.d(TAG, "Response: ${Gson().toJson(response)}")
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                response.message()
                Log.e(
                    TAG, "failed to send due to: \n" +
                            "message: ${response.message()}\n" +
                            "error body: ${response.errorBody()} \n" +
                            "body: ${response.body()} \n" +
                            "headers: ${response.headers()}"
                )

            }
        } catch (e: Exception) {
            Log.e("Notification", "exception >> failed to send notification due to: ${e.message}")
        }
    }
}

fun notifyData(notify: Notification, to: String): PushNotificationData {

    if (notify.internalLink.isNullOrEmpty() && notify.externalLink.isNullOrEmpty()) {
//        val defaultDestination = R.id.home // home screen
//        notify.internalLink = defaultDestination.toString()
//        notify.externalLink = defaultDestination.toString()

    }
    return PushNotificationData(notify, to)
}

fun subscribeToTopic(topic: String) {
    FirebaseMessaging.getInstance().subscribeToTopic(topic)
}

fun getToken(token: (String) -> Unit) {

    FirebaseMessaging.getInstance().token.addOnCompleteListener {
        if (it.isComplete) {
            val t = it.result.toString()
            token(t)
        }

    }

}
