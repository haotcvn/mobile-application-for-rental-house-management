package com.tranconghaoit.homemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tranconghaoit.homemanager.activities.buildings.BuildingActivity
import com.tranconghaoit.homemanager.databinding.ItemBuildingBinding
import com.tranconghaoit.homemanager.models.BuildingModel
import com.tranconghaoit.homemanager.utils.HomeManager

class BuildingAdapter(var context: Context, private var list: List<BuildingModel>) :
    RecyclerView.Adapter<BuildingAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemBuildingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(context, BuildingActivity::class.java)
                    intent.putExtra("homeID", list[position].homeID)
                    context.startActivity(intent)


                    // Lưu danh sách toà nhà để quản lý
                    HomeManager.setHome(list[position])
                }
            }
        }

        fun onBinData(position: Int) {
            with(binding) {
                tvHomeName.text = list[position].name
                tvHomeAddress.text = list[position].address
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBuildingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (list.isNotEmpty()) {
            return list.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBinData(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<BuildingModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}