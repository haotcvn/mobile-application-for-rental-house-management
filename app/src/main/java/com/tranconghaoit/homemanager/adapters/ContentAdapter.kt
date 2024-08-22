package com.tranconghaoit.homemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.databinding.ItemHomeContentBinding
import com.tranconghaoit.homemanager.models.ContentModel

class ContentAdapter(private val context: Context, private val list: MutableList<ContentModel>) :
    RecyclerView.Adapter<ContentAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemHomeContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                when (it) {
                    
                }
            }
        }

        fun bind(position: Int) {
            with(binding) {
                Glide.with(context).load(list[position].image).into(imageContent)
                tvContent.text = list[position].name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomeContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    // Phương thức này để cập nhật dữ liệu mới cho adapter
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<ContentModel>) {
        list.apply {
            clear()
            addAll(newList)
        }
        notifyDataSetChanged()
    }
}