package com.shaimaziyad.khayal1.screens.home

import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.shaimaziyad.khayal1.utils.DisplayableHomeItem
import com.shaimaziyad.khayal1.utils.bannerTypeOneAdapterDelegate
//import com.shaimaziyad.khayal1.utils.bannerTypeTwoAdapterDelegate
import com.shaimaziyad.khayal1.utils.itemNovelWithCategoryAdapterDelegate

// this adapter will display mixed data in home fragment
class HomeAdapter(private val fragment: Home) : ListDelegationAdapter<List<DisplayableHomeItem>>(
    bannerTypeOneAdapterDelegate(),
    itemNovelWithCategoryAdapterDelegate(fragment, GridLayoutManager.HORIZONTAL)
)