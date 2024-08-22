package com.tranconghaoit.homemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.rooms.RoomActivity
import com.tranconghaoit.homemanager.databinding.ItemRoomBinding
import com.tranconghaoit.homemanager.models.RoomModel
import com.tranconghaoit.homemanager.utils.RoomManager

class RoomAdapter(var context: Context, private var list: List<RoomModel>) :
    RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Lưu list
                    RoomManager.setRoom(list[position])

                    val intent = Intent(context, RoomActivity::class.java)
                    intent.putExtra("roomID", list[position].roomID)
                    context.startActivity(intent)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBinData(position: Int) {
            val room = list[position]
            with(binding) {
                tvRoomName.text = room.name
                val decimalFormat = DecimalFormat("###,###,###")
                tvPrice.text = "${decimalFormat.format(room.price)} đ"
                tvStatusItem.text = getStatusString(room.status)
                tvStatusItem.setBackgroundColor(getStatusColor(room.status))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    fun updateData(newList: List<RoomModel>) {
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