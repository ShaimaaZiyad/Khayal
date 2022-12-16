package com.shaimaziyad.khayal1.data

data class PushNotificationData(val data: Notification, val to: String)
// 1- data : data of notification
// 2- to : target to token that will receive notification