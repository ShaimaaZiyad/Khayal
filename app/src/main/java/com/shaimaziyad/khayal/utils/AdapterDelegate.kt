package com.shaimaziyad.khayal.utils

import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.shaimaziyad.khayal.data.Banner
import com.shaimaziyad.khayal.data.Novel
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.NovelDetailsData
import com.shaimaziyad.khayal.databinding.ItemBannerTypeOneBinding
import com.shaimaziyad.khayal.databinding.ItemBannerTypeTwoBinding
import com.shaimaziyad.khayal.databinding.ItemChapterBinding
import com.shaimaziyad.khayal.databinding.ItemChapterNovelBinding
import com.shaimaziyad.khayal.databinding.ItemNovelsWithCategoryBinding
import com.shaimaziyad.khayal.screens.home.Home
import com.shaimaziyad.khayal.screens.home.NovelAdapter
import com.shaimaziyad.khayal.screens.novelDetails.ChapterAdapter
import com.shaimaziyad.khayal.screens.novelDetails.NovelDetails



// display single banner with title, subtitle and descriptions
fun bannerTypeOneAdapterDelegate() = adapterDelegateViewBinding<Banner, DisplayableHomeItem, ItemBannerTypeOneBinding>(
    { layoutInflater, root -> ItemBannerTypeOneBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        binding.banner = item
    }
}


// display single banner with cover only
fun bannerTypeTwoAdapterDelegate() = adapterDelegateViewBinding<Banner, DisplayableHomeItem, ItemBannerTypeTwoBinding>(
    { layoutInflater, root -> ItemBannerTypeTwoBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        binding.banner = item
    }
}



// display category and list of novels
fun itemNovelWithCategoryAdapterDelegate(fragment: Home, orientation: Int) = adapterDelegateViewBinding<NovelData, DisplayableHomeItem, ItemNovelsWithCategoryBinding>(
    { layoutInflater, root -> ItemNovelsWithCategoryBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        binding.tvCategory.text = item.category
        val novelAdapter = NovelAdapter()

        /** on novel clicked **/
        novelAdapter.clickListener = object: NovelAdapter.ClickListener {
            override fun onClick(novel: Novel, index: Int) {
                fragment.navigateToAddEditNovel(true,novel)
            }
        }

        /** button go to category **/
        binding.btnShowCategory.setOnClickListener {
            fragment.navigateToSearch(item.novels)

//            Toast.makeText(binding.tvCategory.context, item.novels.size.toString(),Toast.LENGTH_SHORT).show()
        }

        /** recycler view novels **/
        binding.rvNovels.apply {
            adapter = novelAdapter
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(binding.rvNovels.context,1,orientation,false)
        }
        novelAdapter.submitList(item.novels)
    }
}



// display category and list of novels
fun itemNovelWithCategoryAdapterDelegate(fragment: NovelDetails, orientation: Int) = adapterDelegateViewBinding<NovelData, DisplayableHomeItem, ItemNovelsWithCategoryBinding>(
    { layoutInflater, root -> ItemNovelsWithCategoryBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        binding.tvCategory.text = item.category
        val novelAdapter = NovelAdapter()
        val pdfs = item.novels.map { it.pdfs }

        /** on novel clicked **/
        novelAdapter.clickListener = object: NovelAdapter.ClickListener {
            override fun onClick(novel: Novel, index: Int) {
                fragment.navigateToSelf(novel)
            }
        }

        /** button go to category **/
        binding.btnShowCategory.setOnClickListener {
//            fragment.navigateToSearch(item.novels)

//            Toast.makeText(binding.tvCategory.context, item.novels.size.toString(),Toast.LENGTH_SHORT).show()
        }

        /** recycler view novels **/
        binding.rvNovels.apply {
            adapter = novelAdapter
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(binding.rvNovels.context,1,orientation,false)
        }
        novelAdapter.submitList(item.novels)
    }
}


// display category and list of novels
fun chapterAdapterDelegate(fragment: NovelDetails) = adapterDelegateViewBinding<NovelDetailsData, DisplayableHomeItem, ItemChapterNovelBinding>(
    { layoutInflater, root -> ItemChapterNovelBinding.inflate(layoutInflater, root, false) }
) {
    bind {

        val chapterAdapter = ChapterAdapter()
        chapterAdapter.novelCover = item.cover

        /** on novel clicked **/
        chapterAdapter.clickListener = object: ChapterAdapter.ClickListener {
            override fun onClick(uri: String) {
                fragment.navigateToPdfViewer(uri)
            }
        }

        /** recycler view novels **/
        binding.rvChapters.apply {
            adapter = chapterAdapter
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(binding.rvChapters.context,1,GridLayoutManager.HORIZONTAL,false)
        }

        chapterAdapter.submitList(item.pdfs)

    }
}
