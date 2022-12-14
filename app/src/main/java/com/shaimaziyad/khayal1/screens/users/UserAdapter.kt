package com.shaimaziyad.khayal1.screens.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaimaziyad.khayal1.data.User
import com.shaimaziyad.khayal1.databinding.ItemUserBinding


class UserAdapter : ListAdapter<User, UserAdapter.ViewHolder>(UserDiffUtil) {

    lateinit var clickListener: UserClickListener

    class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }


        fun onBind(data: User, clickListener: UserClickListener) {
            binding.user = data

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

    companion object UserDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

    }

    interface UserClickListener {
        fun onClick(user: User, index: Int)
    }

}
