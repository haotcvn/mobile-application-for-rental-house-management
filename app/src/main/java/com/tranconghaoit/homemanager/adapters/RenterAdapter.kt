package com.tranconghaoit.homemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.renters.RenterDetailedActivity
import com.tranconghaoit.homemanager.databinding.ItemRenterRoomBinding
import com.tranconghaoit.homemanager.models.RoomRenterModel

class RenterAdapter (var context: Context, private var list: List<RoomRenterModel>) :
    RecyclerView.Adapter<RenterAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemRenterRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Lưu list
                    //RoomManager.setRoom(list[position])

                    val intent = Intent(context, RenterDetailedActivity::class.java)
                    intent.putExtra("renterID", list[position].renterID)
                    intent.putExtra("roomName", list[position].roomName)
                    context.startActivity(intent)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBinData(position: Int) {
            val renter = list[position]
            with(binding) {
                tvRenterName.text = renter.renterName
                tvNumberPhone.text = renter.numberPhone
                if (renter.roomName == null) {
                    tvRoomHome.text = "Chưa được thêm vào phòng"
                    tvRoomHome.setTextColor(ContextCompat.getColor(context, R.color.red))
                } else{
                    tvRoomHome.text = renter.roomName
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRenterRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    fun updateData(newList: List<RoomRenterModel>) {
        this.list = newList
        notifyDataSetChanged()
    }

    private fun getStatusString(status: Int): String {
        return if (status == 1) "Đã thuê" else "Trống"
    }

    @SuppressLint("ResourceType")
    private fun getStatusColor(status: Int): Int {
        return if (status == 0) ContextCompat.getColor(context, R.color.lavender) else ContextCompat.getColor(context, R.color.red)
    }
}