package com.shaimaziyad.khayal1.data

import com.shaimaziyad.khayal1.utils.DisplayableHomeItem

data class NovelData(
    val category: String = "",
    val novels: List<Novel> = ArrayList()
) : DisplayableHomeItem
