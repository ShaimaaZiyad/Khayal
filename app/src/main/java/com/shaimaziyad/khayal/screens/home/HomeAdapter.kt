package com.shaimaziyad.khayal.screens.home

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.shaimaziyad.khayal.utils.DisplayableHomeItem
import com.shaimaziyad.khayal.utils.bannerTypeOneAdapterDelegate
import com.shaimaziyad.khayal.utils.promotionsNovelsAdapterDelegate

// this adapter will display mixed data in home fragment
class HomeAdapter(val fragment: Home) : ListDelegationAdapter<List<DisplayableHomeItem>>(
    bannerTypeOneAdapterDelegate(),
    promotionsNovelsAdapterDelegate(fragment,GridLayoutManager.HORIZONTAL)
)