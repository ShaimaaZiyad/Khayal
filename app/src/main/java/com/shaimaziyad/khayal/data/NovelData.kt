package com.shaimaziyad.khayal.data

import com.shaimaziyad.khayal.utils.DisplayableHomeItem

data class NovelData(
    val category: String = "",
    val novels: List<Novel> = ArrayList()
): DisplayableHomeItem
