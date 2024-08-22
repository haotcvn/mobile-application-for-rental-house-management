package com.tranconghaoit.homemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tranconghaoit.homemanager.databinding.ItemRoomRentedBinding
import com.tranconghaoit.homemanager.models.RoomRentedModel

class RoomRentedAdapter(var context: Context, private var list: List<RoomRentedModel>) :
    RecyclerView.Adapter<RoomRentedAdapter.ViewHolder>() {

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(contractID: Int, roomID: Int)
    }

    inner class ViewHolder(var binding: ItemRoomRentedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contractID = list[position].contractID
                    val roomID = list[position].roomID
                    listener?.onItemClick(contractID, roomID)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBinData(position: Int) {
            val room = list[position]
            binding.apply {
                tvRoomHome.text = "${room.roomName} - ${room.homeName}"
                tvAddress.text = room.address
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRoomRentedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBinData(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<RoomRentedModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}