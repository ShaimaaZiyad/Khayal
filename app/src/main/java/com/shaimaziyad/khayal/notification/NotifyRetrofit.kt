package com.shaimaziyad.khayal.notification

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotifyRetrofit {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Notify.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api: NotifyApi by lazy { retrofit.create(NotifyApi::class.java) }
    }

}