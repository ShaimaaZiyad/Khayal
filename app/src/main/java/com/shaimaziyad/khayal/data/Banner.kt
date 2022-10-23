package com.shaimaziyad.khayal.data

import com.shaimaziyad.khayal.utils.DisplayableHomeItem

data class Banner(
    val id: String = "",
    val title: String = "",
    val subTitle: String = "",
    val description: String = "",
    val cover: String = ""
): DisplayableHomeItem
