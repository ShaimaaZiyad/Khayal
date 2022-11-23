package com.shaimaziyad.khayal.screens.bannerManager


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaimaziyad.khayal.data.Banner
import com.shaimaziyad.khayal.databinding.ItemManageBannerBinding
import com.shaimaziyad.khayal.databinding.ItemPdfBinding

class BannerAdapter : ListAdapter<Banner, BannerAdapter.ViewHolder>(BannerDiffCallback) {

    lateinit var clickListener: BannerClickListener

    class ViewHolder(private val binding: ItemManageBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemManageBannerBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }


        // update ui
        @SuppressLint("SetTextI18n")
        fun onBind(data: Banner, clickListener: BannerClickListener) {

            binding.banner = data

            // when click on novel item
            binding.btnEditBanner.setOnClickListener {
                clickListener.onEditBanner(data)
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


    companion object BannerDiffCallback : DiffUtil.ItemCallback<Banner>() {
        override fun areItemsTheSame(oldItem: Banner, newItem: Banner): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Banner, newItem: Banner): Boolean {
            return oldItem == newItem
        }

    }


    interface BannerClickListener {
        fun onEditBanner(banner: Banner)
    }


}

