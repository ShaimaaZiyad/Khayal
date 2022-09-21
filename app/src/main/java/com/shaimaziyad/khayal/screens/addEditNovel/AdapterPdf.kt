package com.shaimaziyad.khayal.screens.addEditNovel


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaimaziyad.khayal.data.PdfData
import com.shaimaziyad.khayal.databinding.ItemPdfBinding

class AdapterPdf : ListAdapter<PdfData, AdapterPdf.ViewHolder>(PdfDiffCallback) {

    lateinit var clickListener: PdfClickListener

    class ViewHolder(private val binding:  ItemPdfBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemPdfBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }


        // update ui
        fun onBind(data: PdfData, clickListener: PdfClickListener) {
            binding.pdf = data

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



    companion object PdfDiffCallback: DiffUtil.ItemCallback<PdfData>(){
        override fun areItemsTheSame(oldItem: PdfData, newItem: PdfData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PdfData, newItem: PdfData): Boolean {
            return oldItem.novelId == newItem.novelId
        }

    }





    interface PdfClickListener {
        fun onRemove(pdf: PdfData, index: Int)
    }



}

