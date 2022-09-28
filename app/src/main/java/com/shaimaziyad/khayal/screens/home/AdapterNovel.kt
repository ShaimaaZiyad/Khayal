package com.shaimaziyad.khayal.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.databinding.RowNovelUserBinding


class AdapterNovel : ListAdapter<NovelData, AdapterNovel.ViewHolder>(NovelDiffCallback) {

    lateinit var clickListener: NovelClickListener

    class ViewHolder(private val binding:  RowNovelUserBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(RowNovelUserBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }


        // update ui
        fun onBind(data: NovelData, clickListener: NovelClickListener) {
            binding.novel = data

            // when click on novel item
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



    companion object NovelDiffCallback: DiffUtil.ItemCallback<NovelData>(){
        override fun areItemsTheSame(oldItem: NovelData, newItem: NovelData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NovelData, newItem: NovelData): Boolean {
            return oldItem.novelId == newItem.novelId
        }

    }


//    fun updateNovel(novel: NovelData, position: Int) {
//        notifyItemChanged(position)
//    }
//
//    fun deleteNovel(){
//
//    }


    interface NovelClickListener {
        fun onClick(novel: NovelData, index: Int)
    }



}