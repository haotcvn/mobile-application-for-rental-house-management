package com.tranconghaoit.homemanager.activities.bills

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityBillDetailedBinding
import com.tranconghaoit.homemanager.models.BillModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.NetworkUtils
import com.tranconghaoit.homemanager.utils.PdfGenerator
import com.tranconghaoit.homemanager.utils.Printer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.FileNotFoundException
import java.text.DecimalFormat

class BillDetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillDetailedBinding
    private var disposable: Disposable? = null
    private var billID: Int = -1
    private var contractID = -1
    private lateinit var mBill: BillModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        billID = intent.getIntExtra("billID", -1)

        setupToolbar()
        setupEvents()
        performGetBillDetailed()
    }

    private fun setupEvents() {
        binding.fabPrint.setOnClickListener {
            try {
                val pdfGenerator = PdfGenerator(this)
                val printer = Printer(this)

                val pdfFile = pdfGenerator.createPdf(mBill)
                printer.printPdf(pdfFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

        binding.btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setMessage("Bạn có chắc chắn muốn xoá hoá đơn này?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                    performDeleteBill()
                }
            }
            dialog.create()
            dialog.show()
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, BillUpdateActivity::class.java)
            intent.putExtra("bill", mBill)
            startActivity(intent)
        }

        binding.btnPayment.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setMessage("Bạn muốn thanh toán hoá đơn này?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Xác nhận") { dialogInterface: DialogInterface, i: Int ->
                    performPaymentBill()
                }
            }
            dialog.create()
            dialog.show()
        }
    }

    private fun performPaymentBill() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (billID > 0) {
                RetrofitClient.apiService.updateBillStatus(billID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ResponseBody> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNext(responseBody: ResponseBody) {
                            val response = responseBody.string()
                            toast(response)

                            if (response == "Thanh toán thành công"){
                                finish()
                            }
                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        } else {
            NetworkUtils.showToast(this)
        }
    }

    private fun performDeleteBill() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (billID > 0) {
                RetrofitClient.apiService.deleteBill(billID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ResponseBody> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNext(responseBody: ResponseBody) {
                            val response = responseBody.string()
                            toast(response)

                            if (response == "Xoá hoá đơn thành công"){
                                finish()
                            }
                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        } else {
            NetworkUtils.showToast(this)
        }
    }

    private fun performGetBillDetailed() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (billID > 0) {
            RetrofitClient.apiService.getBillDetailed(billID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BillModel> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onNext(bill: BillModel) {
                        binding.apply {
                            progressBar.visibility = View.GONE
                            mBill = bill
                            contractID = bill.contractID

                            val issueDate = DateFormatUtils.convertDateFormat(bill.issueDate)
                            val fromDate = DateFormatUtils.convertDateFormat(bill.fromDate)
                            val toDate = DateFormatUtils.convertDateFormat(bill.toDate)
                            val decimalFormat = DecimalFormat("###,###,###")

                            tvBillID.text = "#${bill.billID}"
                            tvIssueDate.text = issueDate

                            tvRoomHome.text = bill.roomName
                            tvNoteDate.text = "Tiền phòng từ $fromDate đến $toDate"

                            tvNoteService.text = "Tiền dịch vụ từ $fromDate đến $toDate và tiền điện nước (nếu có) đã sử dụng trước đó"
                            tvNoteDuration.text = "Hạn thanh toán từ $issueDate đến $issueDate"

                            tvRoomPrice.text = "${decimalFormat.format(bill.roomPrice)} đ"
                            tvServicePrice.text = "${decimalFormat.format(bill.servicePrice)} đ"
                            tvBonusPrice.text = "0 đ"
                            tvTotal.text = "${decimalFormat.format(bill.total)} đ"
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        } else {
            NetworkUtils.showToast(this)
        }
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}