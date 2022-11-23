package com.shaimaziyad.khayal.screens.home

import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.shaimaziyad.khayal.utils.DisplayableHomeItem
import com.shaimaziyad.khayal.utils.bannerTypeOneAdapterDelegate
//import com.shaimaziyad.khayal.utils.bannerTypeTwoAdapterDelegate
import com.shaimaziyad.khayal.utils.itemNovelWithCategoryAdapterDelegate

// this adapter will display mixed data in home fragment
class HomeAdapter(private val fragment: Home) : ListDelegationAdapter<List<DisplayableHomeItem>>(
    bannerTypeOneAdapterDelegate(),
    itemNovelWithCategoryAdapterDelegate(fragment, GridLayoutManager.HORIZONTAL)
)