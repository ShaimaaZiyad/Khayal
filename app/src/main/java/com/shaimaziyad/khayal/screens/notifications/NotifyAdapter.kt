package com.shaimaziyad.khayal.screens.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaimaziyad.khayal.data.Notification
import com.shaimaziyad.khayal.databinding.ItemNotificationBinding

class NotifyAdapter : ListAdapter<Notification, NotifyAdapter.ViewHolder>(NotifyDiffCallback) {

    lateinit var clickListener: NotifyClickListener

    class ViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }


        fun onBind(data: Notification, clickListener: NotifyClickListener) {

            binding.notification = data

            /** onItem clicked **/
            binding.btnItem.setOnClickListener {
                clickListener.onClick(data,adapterPosition)
            }

            /** onLong Item clicked **/
            binding.btnItem.setOnLongClickListener {
                clickListener.onLongClick(data,adapterPosition,binding.btnItem)
                true
            }

            binding.executePendingBindings()
        }



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.binding(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notify = getItem(position)
        holder.onBind(notify,clickListener)
    }



    companion object NotifyDiffCallback: DiffUtil.ItemCallback<Notification>(){
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

    }


    interface NotifyClickListener {
        fun onClick(notify: Notification, index: Int)
        fun onLongClick(notify: Notification, index: Int,v: View)
    }


}