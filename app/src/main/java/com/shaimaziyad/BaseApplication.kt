package com.shaimaziyad

import android.app.Application
import com.shaimaziyad.khayal.di.extraModules
import com.shaimaziyad.khayal.di.repoModules
import com.shaimaziyad.khayal.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication(): Application() {


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(repoModules, viewModelModules, extraModules))
        }

    }
}