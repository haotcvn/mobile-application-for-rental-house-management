package com.tranconghaoit.homemanager.activities.contracts

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databases.BillDAO
import com.tranconghaoit.homemanager.databinding.ActivityContractDetailedBinding
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

class ContractDetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContractDetailedBinding
    private var disposable: Disposable? = null
    private var contractID: Int = -1
    private lateinit var contract: ContractDetailedModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contractID = intent.getIntExtra("contractID", -1)

        setupToolbar()
        setupEvents()
        performGetContractDetailed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupEvents() {
        binding.btnContractEdit.setOnClickListener {
            val intent = Intent(this@ContractDetailedActivity,ContractUpdateActivity::class.java)
            intent.putExtra("contract", contract)
            startActivity(intent)
        }

        binding.btnContractDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setTitle("Xác nhận")
                setMessage("Bạn có chắc chắn muốn xoá hợp đồng này?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                    performDeleteContract()
                }
            }
            dialog.create()
            dialog.show()
        }

        binding.btnContractTermination.setOnClickListener {
            binding.btnContractTermination.setOnClickListener {
                val bill = BillModel(
                    contract.contractID,
                    contract.roomID,
                    DateFormatUtils.convertToMySQLDate(DateFormatUtils.getCurrentDateAsString()).toString(),
                    DateFormatUtils.convertToMySQLDate(contract.startDate).toString(),
                    DateFormatUtils.convertToMySQLDate(DateFormatUtils.dateAfterOneMonth(contract.startDate, contract.duration)).toString(),
                    contract.price,
                    0,
                    "",
                    0
                )
                val dialog = AlertDialog.Builder(this)
                dialog.apply {
                    setTitle("Xác nhận")
                    setMessage("Bạn có chắc chắn muốn thanh hợp đồng này?")
                    setCancelable(false)
                    setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                        BillDAO(this@ContractDetailedActivity).performAddBill(bill)
                        performDeleteContract()
                    }
                }
                dialog.create()
                dialog.show()
            }
        }
    }

    private fun performDeleteContract() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (contract.roomID > 0) {
                RetrofitClient.apiService.deleteContract(contract.roomID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ResponseBody> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(responseBody: ResponseBody) {
                            val response = responseBody.string()
                            toast(response)
                            if (response == "Xoá hợp đồng thành công") {
                                finish()
                            }
                        }

                        override fun onError(e: Throwable) {
                            toast("Không thể thực hiện")
                        }

                        override fun onComplete() {

                        }
                    })
            }
        } else {
            NetworkUtils.showToast(this)
        }
    }/* END performDeleteRoom() */

    private fun performGetContractDetailed() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (contractID > 0) {
                RetrofitClient.apiService.getContractDetailed(contractID, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ContractDetailedModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNext(obj: ContractDetailedModel) {
                            contract = obj
                            binding.tvContractID.text = "#${obj.contractID}"
                            binding.tvStartDay.text = obj.startDate
                            binding.tvRoomHome.text = obj.roomName
                            val startDay = obj.startDate
                            val endDay = if (obj.endDate == "00-00-0000") "[Chưa xác định thời gian]" else obj.endDate
                            binding.tvDate.text = "Từ $startDay đến $endDay"
                            binding.tvCreator.text = "Người tạo: ${obj.creator}"
                            binding.tvPrice.text = "${DecimalFormat("###,###,###").format(obj.price)} đ"
                            binding.tvDeposits.text = "${DecimalFormat("###,###,###").format(obj.deposit)} đ"
                            binding.tvDuration.text = "${obj.duration} tháng"
                            binding.tvRenterName.text = obj.renterName
                            binding.tvNumberPhone.text = obj.numberPhone
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

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

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