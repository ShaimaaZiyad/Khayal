package com.shaimaziyad.khayal.data

import com.shaimaziyad.khayal.utils.DisplayableHomeItem

data class BannerData(
    val banners: List<Banner> = ArrayList()
) : DisplayableHomeItem
