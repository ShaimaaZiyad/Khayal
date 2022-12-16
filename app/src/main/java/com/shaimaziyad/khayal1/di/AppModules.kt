package com.shaimaziyad.khayal1.di

import com.shaimaziyad.khayal1.remote.DataBase
import com.shaimaziyad.khayal1.repository.*
import com.shaimaziyad.khayal1.screens.addEditNovel.AddEditNovelViewModel
import com.shaimaziyad.khayal1.screens.auth.AuthViewModel
import com.shaimaziyad.khayal1.screens.bannerManager.BannerManagerViewModel
import com.shaimaziyad.khayal1.screens.home.HomeViewModel
import com.shaimaziyad.khayal1.screens.notifications.NotificationsViewModel
import com.shaimaziyad.khayal1.screens.pdfViewer.PdfViewerViewModel
import com.shaimaziyad.khayal1.screens.profile.ProfileViewModel
import com.shaimaziyad.khayal1.screens.users.UsersViewModel
import com.shaimaziyad.khayal1.utils.SharePrefManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { HomeViewModel(context = androidContext(), userRepo = get(), novelRepo = get()) }
    viewModel { AuthViewModel(userRepo = get()) }
    viewModel { BannerManagerViewModel(bannerRepo = get()) }
    viewModel { NotificationsViewModel(notifyRepo = get(), userRepo = get()) }
    viewModel { PdfViewerViewModel(pdfRepo = get()) }
    viewModel { ProfileViewModel(userRepo = get()) }
    viewModel { UsersViewModel(userRepo = get()) }
    viewModel { AddEditNovelViewModel(novelRepo = get(), pdfRepo = get()) }
}


val repoModules = module {
    single { BannerRepository(remote = get()) }
    single { UserRepository(remote = get(), sharePref = get()) }
    single { NovelRepository(remote = get()) }
    single { NotifyRepository(remote = get(), userRepo = get()) }
    single { PdfRepository(remote = get()) }
}


val extraModules = module {
    single { SharePrefManager(context = androidContext()) }
    single { DataBase() }
}


