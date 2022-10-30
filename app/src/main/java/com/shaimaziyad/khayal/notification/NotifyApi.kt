package com.shaimaziyad.khayal.notification


import com.shaimaziyad.khayal.data.PushNotificationData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotifyApi {

    @Headers("Authorization: key=${Notify.SERVER_KEY}", "Content-Type:${Notify.CONTENT_TYPE}")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotificationData
    ): Response<ResponseBody>

}