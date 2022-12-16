package com.shaimaziyad.khayal1.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaimaziyad.khayal1.data.Novel
import com.shaimaziyad.khayal1.databinding.ItemNovelBinding


class NovelAdapter : ListAdapter<Novel, NovelAdapter.ViewHolder>(NovelDiffCallback) {

    lateinit var clickListener: ClickListener

    class ViewHolder(private val binding: ItemNovelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemNovelBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }

        fun onBind(data: Novel, clickListener: ClickListener) {
            binding.novel = data

            /** on item clicked **/
            binding.btnItem.setOnClickListener {
                clickListener.onClick(data, adapterPosition)
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.binding(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val novel = getItem(position)
        holder.onBind(novel, clickListener)
    }

    companion object NovelDiffCallback : DiffUtil.ItemCallback<Novel>() {
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