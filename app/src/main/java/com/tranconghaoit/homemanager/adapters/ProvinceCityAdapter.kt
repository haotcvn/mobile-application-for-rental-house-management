package com.tranconghaoit.homemanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.tranconghaoit.homemanager.databinding.ActivityBuildingAddBinding
import com.tranconghaoit.homemanager.databinding.ItemProvinceCityBinding
import com.tranconghaoit.homemanager.models.ProvinceModel

class ProvinceCityAdapter(context: Context, objects: MutableList<ProvinceModel>) : ArrayAdapter<ProvinceModel>(context, 0, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ActivityBuildingAddBinding

        if (convertView == null) {
            binding = ActivityBuildingAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ActivityBuildingAddBinding
        }

        val item = this.getItem(position)
        if (item != null) {
            //binding.edtProvinceCity.setText(item?.name?.toString())
        } // Sử dụng tên trường dữ liệu thích hợp

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemProvinceCityBinding

        if (convertView == null) {
            binding = ItemProvinceCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ItemProvinceCityBinding
        }

        val item = this.getItem(position)
        binding.tvProvinceCity.text = item?.name // Sử dụng tên trường dữ liệu thích hợp

        return binding.root
    }
}
