package com.tranconghaoit.homemanager.utils

import android.annotation.SuppressLint
import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateFormatUtils {

    // Hàm chuyển đổi ngày từ dạng "dd-MM-yyyy" sang "yyyy-MM-dd"
    fun convertToMySQLDate(editText: EditText): String? {
        val dateText = editText.text.toString()
        val dateParts = dateText.split("-")
        if (dateParts.size != 3) {
            // Đảm bảo rằng định dạng đúng gồm ngày, tháng và năm
            return null
        }
        var day = dateParts[0]
        var month = dateParts[1]
        val year = dateParts[2]

        // Đảm bảo ngày, tháng và năm có đủ chữ số
        if (day.length == 1) {
            day = "0$day"
        }
        if (month.length == 1) {
            month = "0$month"
        }

        // Tạo chuỗi ngày theo định dạng MySQL
        return "$year-$month-$day"
    }

    // Hàm chuyển đổi ngày từ dạng "dd-MM-yyyy" sang "yyyy-MM-dd"
    fun convertToMySQLDate(dateText: String): String? {
        val dateParts = dateText.split("-")
        if (dateParts.size != 3) {
            // Đảm bảo rằng định dạng đúng gồm ngày, tháng và năm
            return  null
        }
        var day = dateParts[0]
        var month = dateParts[1]
        val year = dateParts[2]

        // Đảm bảo ngày, tháng và năm có đủ chữ số
        if (day.length == 1) {
            day = "0$day"
        }
        if (month.length == 1) {
            month = "0$month"
        }

        return "$year-$month-$day"
    }


    // Hàm chuyển đổi ngày từ dạng "yyyy-MM-dd" sang "dd-MM-yyyy"
    @SuppressLint("SimpleDateFormat")
    fun convertDateFormat(inputDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    // Hàm lấy ngày hiện tại
    fun getCurrentDateAsString(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    fun getCurrentMonthAsString(): String {
        val dateFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateAfterOneMonth(oldDate: String, duration: Int): String {
        // Date string format
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        // Convert the date string to a LocalDate object
        val oldDateLocalDate = LocalDate.parse(oldDate, formatter)

        // Calculate the date after one month
        val newDateLocalDate = oldDateLocalDate.plusMonths(duration.toLong())

        // Convert the LocalDate object to a new date string
        return newDateLocalDate.format(formatter)
    }
}