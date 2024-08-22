package com.tranconghaoit.homemanager.activities.bills

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityBillUpdateBinding
import com.tranconghaoit.homemanager.models.BillModel
import com.tranconghaoit.homemanager.models.ContractDetailedModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.DecimalFormat

class BillUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillUpdateBinding
    private lateinit var contractT: ContractDetailedModel
    private lateinit var bill: BillModel
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        bill = intent.getSerializableExtra("bill") as BillModel

        performGetContractDetailed()
        showInformation()
    }

    private fun showInformation() {
        binding.apply {
            edtMonth.setText(DateFormatUtils.convertDateFormat(bill.issueDate))
        }
    }


    private fun performGetContractDetailed() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (bill.contractID > 0) {
                RetrofitClient.apiService.getContractDetailed(bill.contractID, 0)
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

                                edtRoomHome.setText(contract.roomName)
                                edtPaymentDate.setText(DateFormatUtils.getCurrentDateAsString()).toString().trim()
                                edtLimitDate.setText(DateFormatUtils.getCurrentDateAsString()).toString().trim()
                                tvContractID.text = "Hợp đồng #${contract.contractID}"
                                val startDay = DateFormatUtils.convertDateFormat(contract.startDate).trim()
                                val endDay =
                                    if (contract.endDate == "0000-00-00") "[Chưa xác định thời gian]" else DateFormatUtils.convertDateFormat(
                                        contract.endDate
                                    ).trim()
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