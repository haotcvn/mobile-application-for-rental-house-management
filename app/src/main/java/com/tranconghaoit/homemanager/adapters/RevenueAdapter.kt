package com.tranconghaoit.homemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tranconghaoit.homemanager.databinding.ItemRevenueBinding
import com.tranconghaoit.homemanager.models.RevenueModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import java.text.DecimalFormat

class RevenueAdapter (var context: Context, private var list: List<RevenueModel>) :
    RecyclerView.Adapter<RevenueAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemRevenueBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        init {
//            binding.root.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val intent = Intent(context, BillDetailedActivity::class.java)
//                    //intent.putExtra("billID", list[position].billID)
//                    context.startActivity(intent)
//                }
//            }
//        }

        @SuppressLint("SetTextI18n")
        fun onBinData(position: Int) {
            val obj = list[position]
            binding.apply {
                tvMonth.text = obj.month
                tvYear.text = "- ${obj.year}"
                tvSum.text = DecimalFormat("###,###,###").format(obj.sum)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRevenueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBinData(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<RevenueModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}