package com.shaimaziyad.khayal.data

import com.shaimaziyad.khayal.utils.DisplayableHomeItem

data class NovelDetailsData(
    val cover: String,
    val pdfs: List<String>
): DisplayableHomeItem
