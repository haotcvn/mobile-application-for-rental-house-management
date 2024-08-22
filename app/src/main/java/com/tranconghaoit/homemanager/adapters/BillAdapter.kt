package com.tranconghaoit.homemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tranconghaoit.homemanager.activities.bills.BillDetailedActivity
import com.tranconghaoit.homemanager.databinding.ItemBillBinding
import com.tranconghaoit.homemanager.models.BillModel
import java.text.DecimalFormat

class BillAdapter(var context: Context, private var list: List<BillModel>) :
    RecyclerView.Adapter<BillAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemBillBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(context, BillDetailedActivity::class.java)
                    intent.putExtra("billID", list[position].billID)
                    context.startActivity(intent)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBinData(position: Int) {
            val bill = list[position]
            binding.apply {
                tvBillID.text = "#${bill.billID}"
                tvTotal.text = "${DecimalFormat("###,###,###").format(bill.total)} đ"
                tvRoomHome.text = bill.roomName
                tvRoomPrice.text = "${DecimalFormat("###,###,###").format(bill.roomPrice)} đ"
                tvServicePrice.text = "${DecimalFormat("###,###,###").format(bill.servicePrice)} đ"
                tvBonusPrice.text = "0 đ"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBinData(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<BillModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}