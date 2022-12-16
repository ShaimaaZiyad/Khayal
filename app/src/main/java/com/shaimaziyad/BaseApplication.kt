package com.shaimaziyad

import android.app.Application
import com.shaimaziyad.khayal1.di.extraModules
import com.shaimaziyad.khayal1.di.repoModules
import com.shaimaziyad.khayal1.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication() : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(repoModules, viewModelModules, extraModules))
        }

    }
}