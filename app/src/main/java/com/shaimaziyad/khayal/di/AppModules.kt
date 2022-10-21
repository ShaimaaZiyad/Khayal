package com.shaimaziyad.khayal.di


import com.shaimaziyad.khayal.remote.DataBase
import com.shaimaziyad.khayal.repository.NotifyRepository
import com.shaimaziyad.khayal.repository.NovelRepository
import com.shaimaziyad.khayal.repository.PdfRepository
import com.shaimaziyad.khayal.repository.UserRepository
import com.shaimaziyad.khayal.screens.addEditNovel.AddEditNovelViewModel
import com.shaimaziyad.khayal.screens.auth.AuthViewModel
import com.shaimaziyad.khayal.screens.home.HomeViewModel
import com.shaimaziyad.khayal.screens.notifications.NotificationsViewModel
import com.shaimaziyad.khayal.screens.pdfViewer.PdfViewerViewModel
import com.shaimaziyad.khayal.screens.profile.ProfileViewModel
import com.shaimaziyad.khayal.screens.users.UsersViewModel
import com.shaimaziyad.khayal.utils.SharePrefManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModules = module {
    viewModel { HomeViewModel(userRepo = get(), novelRepo = get() ) }
    viewModel { AuthViewModel(userRepo = get())  }
    viewModel { NotificationsViewModel(notifyRepo = get(), userRepo = get()) }
    viewModel { PdfViewerViewModel(pdfRepo = get()) }
    viewModel { ProfileViewModel(userRepo = get()) }
    viewModel { UsersViewModel(userRepo = get()) }
    viewModel { AddEditNovelViewModel(novelRepo = get(), pdfRepo = get() ) }
}


val repoModules = module {
    single { UserRepository(remote = get(), sharePref = get()) }
    single { NovelRepository(remote = get()) }
    single { NotifyRepository(remote = get() , userRepo = get()) }
    single { PdfRepository(remote = get()) }
}


val extraModules = module {
    single { SharePrefManager( context = androidContext() ) }
    single { DataBase() }
}


