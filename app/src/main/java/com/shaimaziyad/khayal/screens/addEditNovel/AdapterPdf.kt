package com.shaimaziyad.khayal.screens.addEditNovel


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaimaziyad.khayal.databinding.ItemPdfBinding

class AdapterPdf : ListAdapter<String, AdapterPdf.ViewHolder>(PdfDiffCallback) {

    lateinit var clickListener: PdfClickListener

    class ViewHolder(private val binding:  ItemPdfBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemPdfBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }


        // update ui
        @SuppressLint("SetTextI18n")
        fun onBind(data: String, clickListener: PdfClickListener) {

            binding.badgeNumber.text = (adapterPosition + 1 ).toString()

            // when click on novel item
            binding.btnRemove.setOnClickListener {
                clickListener.onRemove(data,adapterPosition)
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



    companion object PdfDiffCallback: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }


    interface PdfClickListener {
        fun onRemove(pdf: String, index: Int)
    }


}

