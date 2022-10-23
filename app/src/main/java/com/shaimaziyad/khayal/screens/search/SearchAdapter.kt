package com.shaimaziyad.khayal.screens.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.shaimaziyad.khayal.data.Novel
import com.shaimaziyad.khayal.databinding.ItemNovelBinding
import com.shaimaziyad.khayal.databinding.ItemNovelCategoryBinding
import com.shaimaziyad.khayal.utils.DisplayableHomeItem
import com.shaimaziyad.khayal.utils.bannerTypeOneAdapterDelegate
import com.shaimaziyad.khayal.utils.promotionsNovelsAdapterDelegate



class SearchAdapter : ListAdapter<Novel, SearchAdapter.ViewHolder>(NovelDiffCallback) {

    lateinit var clickListener: ClickListener

    class ViewHolder(private val binding: ItemNovelCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemNovelCategoryBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }

        fun onBind(data: Novel, clickListener: ClickListener) {
            binding.novel = data

            /** on item clicked **/
            binding.btnItem.setOnClickListener {
                clickListener.onClick(data,adapterPosition)
            }
            binding.executePendingBindings()
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.binding(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val novel = getItem(position)
        holder.onBind(novel,clickListener)
    }



    companion object NovelDiffCallback: DiffUtil.ItemCallback<Novel>() {
        override fun areItemsTheSame(oldItem: Novel, newItem: Novel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Novel, newItem: Novel): Boolean {
            return oldItem.novelId == newItem.novelId
        }

    }


    interface ClickListener {
        fun onClick(novel: Novel, index: Int)
    }


}