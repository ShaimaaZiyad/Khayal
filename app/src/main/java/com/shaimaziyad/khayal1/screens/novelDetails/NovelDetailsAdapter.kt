package com.shaimaziyad.khayal1.screens.novelDetails

import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.shaimaziyad.khayal1.utils.*

// this adapter will display mixed data in home fragment
class NovelDetailsAdapter(private val fragment: NovelDetails) :
    ListDelegationAdapter<List<DisplayableHomeItem>>(
        bannerTypeOneAdapterDelegate(),
        itemNovelWithCategoryAdapterDelegate(fragment, GridLayoutManager.HORIZONTAL),
        chapterAdapterDelegate(fragment)
    )