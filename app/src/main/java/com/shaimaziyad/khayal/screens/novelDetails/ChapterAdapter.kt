package com.shaimaziyad.khayal.screens.novelDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaimaziyad.khayal.databinding.ItemChapterBinding


class ChapterAdapter : ListAdapter<String, ChapterAdapter.ViewHolder>(ChapterDiffCallback) {

    lateinit var clickListener: ClickListener
    lateinit var novelCover: String

    class ViewHolder(private val binding:  ItemChapterBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemChapterBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }

        fun onBind(data: String, clickListener: ClickListener) {

            binding.cover = data
            val chapterNumber =  adapterPosition + 1

            binding.badgeNumber.text = chapterNumber.toString()

            /** on item clicked **/
            binding.btnItem.setOnClickListener {
                clickListener.onClick(data)
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


    companion object ChapterDiffCallback: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }


    interface ClickListener {
        fun onClick(uri: String)
    }



}