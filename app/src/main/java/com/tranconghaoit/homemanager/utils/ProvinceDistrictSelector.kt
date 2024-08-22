package com.tranconghaoit.homemanager.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.tranconghaoit.homemanager.databinding.DialogProvinceBinding
import com.tranconghaoit.homemanager.models.ProvinceModel
import java.util.Locale

class ProvinceDistrictSelector(private val context: Context) {
    private lateinit var dialog: AlertDialog
    var provinceID = ""
    var isSelectedProvince = true

    fun showProvinceDistrict(list: List<ProvinceModel>, editText: EditText) {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = DialogProvinceBinding.inflate(LayoutInflater.from(context))
        builder.setView(dialogBinding.root)

        val provinceNames = list.map { it.name }

        val mAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            provinceNames
        )
        dialogBinding.lvProvince.adapter = mAdapter

        if (!isSelectedProvince) {
            dialogBinding.tvTitle.text = "Chọn Quận/Huyện"
        }

        val filteredList = mutableListOf<ProvinceModel>() // Danh sách lọc
        dialogBinding.lvProvince.setOnItemClickListener { parent, view, position, id ->
            val selectedProvince = if (filteredList.isNotEmpty()) filteredList[position] else list[position]
            editText.setText(selectedProvince.name)
            if (isSelectedProvince) {
                provinceID = selectedProvince.provinceID
            }
            dialog.dismiss()
        }

        dialogBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filteredList.clear()
                filteredList.addAll(list.filter { province ->
                    province.name.lowercase(Locale.ROOT).contains(newText.lowercase(Locale.ROOT))
                })
                if (filteredList.isEmpty()) {
                    dialogBinding.tvNull.visibility = View.VISIBLE
                } else {
                    dialogBinding.tvNull.visibility = View.GONE
                }
                mAdapter.clear()
                mAdapter.addAll(filteredList.map { it.name })
                mAdapter.notifyDataSetChanged()
                return true
            }
        })

        dialogBinding.btnExit.setOnClickListener {
            dialog.dismiss()
        }

        dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()

    }
}
