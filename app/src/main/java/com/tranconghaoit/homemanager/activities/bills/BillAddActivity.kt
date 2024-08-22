@file:Suppress("DEPRECATION")

package com.tranconghaoit.homemanager.activities.bills

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import com.tranconghaoit.homemanager.activities.rooms.RoomRentedActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityBillAddBinding
import com.tranconghaoit.homemanager.models.BillModel
import com.tranconghaoit.homemanager.models.ContractDetailedModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.text.DecimalFormat

class BillAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillAddBinding
    private lateinit var contractT: ContractDetailedModel
    private var contractID = -1
    private var roomID: Int = -1
    private var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupEvents()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            contractID = data?.getIntExtra("contractID", -1)!!
            roomID = data?.getIntExtra("roomID", -1)!!
        }
    }

    private fun setupEvents() {
        binding.apply {
            layoutContract.visibility = View.GONE
            layoutDeposits.visibility = View.GONE
            layoutService.visibility = View.GONE
            layoutRoomPrice.visibility = View.GONE

            edtMonth.setText(DateFormatUtils.getCurrentDateAsString())
        }

        binding.edtRoomHome.setOnClickListener {
            val intent = Intent(this, RoomRentedActivity::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.btnBillAdd.setOnClickListener {
            val issueMonth = DateFormatUtils.convertToMySQLDate(binding.edtMonth.text.toString()).toString().trim()
            val fromDate = DateFormatUtils.convertToMySQLDate(binding.edtFromDate.text.toString()).toString().trim()
            val toDate = DateFormatUtils.convertToMySQLDate(binding.edtToDate.text.toString()).toString().trim()
            val note = binding.edtBillNote.text.toString().trim()
            val roomPriceText = 0 //binding.tvSumPrice.text.toString().trim()
            val servicePriceText = 0 // binding.tvSumService.text.toString().trim()

            val roomPrice = contractT.price
            val servicePrice = 0

            val bill = BillModel(
                contractID,
                roomID,
                issueMonth,
                fromDate,
                toDate,
                roomPrice,
                servicePrice,
                note,
                0
            )
            //toast(bill.toString())
            performAddBill(bill)
        }
    }


    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun performAddBill(bill: BillModel) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.addBill(bill)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)
                        if (response == "Tạo hóa đơn thành công") {
                            finish()
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
        } else {
            NetworkUtils.showToast(this)
        }
    }/* END performAddBill() */

    private fun performGetContractDetailed() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (contractID > 0) {
                RetrofitClient.apiService.getContractDetailed(contractID,0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ContractDetailedModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @RequiresApi(Build.VERSION_CODES.O)
                        @SuppressLint("SetTextI18n")
                        override fun onNext(contract: ContractDetailedModel) {
                            contractT = contract
                            binding.apply {
                                layoutContract.visibility = View.VISIBLE
                                layoutDeposits.visibility = View.VISIBLE
                                layoutService.visibility = View.VISIBLE
                                layoutRoomPrice.visibility = View.VISIBLE

                                edtRoomHome.setText(contract.roomName)
                                edtPaymentDate.setText(DateFormatUtils.getCurrentDateAsString()).toString().trim()
                                edtLimitDate.setText(DateFormatUtils.getCurrentDateAsString()).toString().trim()
                                tvContractID.text = "Hợp đồng #${contract.contractID}"
                                val startDay = contract.startDate
                                val endDay =
                                    if (contract.endDate == "00-00-0000") "[Chưa xác định thời gian]" else contract.endDate
                                tvDate.text = "Từ $startDay đến $endDay"
                                edtFromDate.setText(startDay)
                                edtToDate.setText(DateFormatUtils.dateAfterOneMonth(startDay, contract.duration))
                                tvPrice.text =
                                    "${DecimalFormat("###,###,###").format(contract.price)} đ/${contract.duration} tháng"
                                tvRoomPrice.text =
                                    DecimalFormat("###,###,###").format(contract.price)
                                tvSumPrice.text =
                                    DecimalFormat("###,###,###").format(contract.price)
                                tvDeposits.text =
                                    DecimalFormat("###,###,###").format(contract.deposit)
                                tvSumDeposits.text =
                                    DecimalFormat("###,###,###").format(contract.deposit)

                                val sumPrice = contract.price
                                val sumService = contract.deposit
                                val total = sumPrice + sumService
                                tvTotal.text = "${DecimalFormat("###,###,###").format(total)} đ"
                            }
                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })
            }
        } else {
            NetworkUtils.showToast(this)
        }
    }/* END performGetContract() */

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        performGetContractDetailed()

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}