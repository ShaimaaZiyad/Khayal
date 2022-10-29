package com.shaimaziyad.khayal.screens.novelDetails

import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.shaimaziyad.khayal.utils.*

// this adapter will display mixed data in home fragment
class NovelDetailsAdapter(private val fragment: NovelDetails) : ListDelegationAdapter<List<DisplayableHomeItem>>(
    bannerTypeOneAdapterDelegate(),
    bannerTypeTwoAdapterDelegate(),
    itemNovelWithCategoryAdapterDelegate(fragment, GridLayoutManager.HORIZONTAL),
    chapterAdapterDelegate(fragment)
)