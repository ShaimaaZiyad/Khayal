package com.shaimaziyad.khayal1.data

import com.shaimaziyad.khayal1.utils.DisplayableHomeItem

data class NovelDetailsData(
    val cover: String,
    val pdfs: List<String>
) : DisplayableHomeItem
