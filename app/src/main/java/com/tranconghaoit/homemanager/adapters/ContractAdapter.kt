package com.tranconghaoit.homemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tranconghaoit.homemanager.activities.contracts.ContractDetailedActivity
import com.tranconghaoit.homemanager.databinding.ItemContractBinding
import com.tranconghaoit.homemanager.models.ContractDetailedModel

class ContractAdapter (var context: Context, private var list: List<ContractDetailedModel>) :
    RecyclerView.Adapter<ContractAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemContractBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(context, ContractDetailedActivity::class.java)
                    intent.putExtra("contractID", list[position].contractID)
                    context.startActivity(intent)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBinData(position: Int) {
            val obj = list[position]
            binding.apply {
                tvContractID.text = "#${obj.contractID}"
                tvRoomHome.text = obj.roomName
                val startDay = obj.startDate
                val endDay =
                    if (obj.endDate == "00-00-0000") "[Chưa xác định thời gian]" else obj.endDate
                tvDate.text = "Từ $startDay đến $endDay"
                tvCreator.text = "Người tạo: ${obj.creator}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemContractBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBinData(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<ContractDetailedModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}