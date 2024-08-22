package com.tranconghaoit.homemanager.activities.contracts

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityContractUpdateBinding
import com.tranconghaoit.homemanager.models.ContractDetailedModel
import com.tranconghaoit.homemanager.models.ContractModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.NetworkUtils
import com.tranconghaoit.homemanager.utils.RoomManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.util.Calendar

class ContractUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContractUpdateBinding
    private lateinit var contract: ContractDetailedModel
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupEvents()
        contract = intent.getSerializableExtra("contract") as ContractDetailedModel
        showInformation()
    }

    private fun showInformation() {
        binding.apply {
            edtRoom.setText(contract.roomName)
            edtStartDay.setText(contract.startDate)
            edtEndDay.setText(contract.endDate)
            edtPriceDateStart.setText(contract.startDate)
            edtDuration.setText(contract.duration.toString())
            edtPrice.setText(contract.price.toString())
            edtDeposits.setText(contract.deposit.toString())
            tvRenterName.text = contract.renterName
            tvNumberPhone.text = contract.numberPhone
        }
    }

    private fun setupEvents() {
        val toDay = Calendar.getInstance()
        val startDay = toDay.get(Calendar.DAY_OF_MONTH)
        val startMonth = toDay.get(Calendar.MONTH)
        val startYear = toDay.get(Calendar.YEAR)
        binding.edtStartDay.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val formattedDay = String.format("%02d", dayOfMonth)
                    val formattedMonth = String.format("%02d", month + 1)
                    binding.edtStartDay.setText("$formattedDay-${formattedMonth}-$year")
                },
                startYear,
                startMonth,
                startDay
            ).show()
        }

        binding.edtEndDay.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val formattedDay = String.format("%02d", dayOfMonth)
                    val formattedMonth = String.format("%02d", month + 1)
                    binding.edtEndDay.setText("$formattedDay-${formattedMonth}-$year")
                },
                startYear,
                startMonth,
                startDay
            ).show()
        }

        binding.edtPriceDateStart.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val formattedDay = String.format("%02d", dayOfMonth)
                    val formattedMonth = String.format("%02d", month + 1)
                    binding.edtPriceDateStart.setText("$formattedDay-${formattedMonth}-$year")
                },
                startYear,
                startMonth,
                startDay
            ).show()
        }

        binding.btnUpdate.setOnClickListener {
            val roomID = RoomManager.getRoom().roomID
            val startDate = DateFormatUtils.convertToMySQLDate(binding.edtStartDay).toString()
            val endDate = DateFormatUtils.convertToMySQLDate(binding.edtEndDay).toString()
            val duration = binding.edtDuration.text.toString().trim()
            val priceText = binding.edtPrice.text.toString().trim()
            val depositText = binding.edtDeposits.text.toString().trim()

            val price = if (priceText.isNotEmpty()) priceText.toInt() else 0
            val deposit = if (depositText.isNotEmpty()) depositText.toInt() else 0

            val contract = ContractModel(
                contract.contractID,
                contract.renterID,
                roomID,
                startDate,
                endDate,
                duration.toInt(),
                price,
                deposit,
                0
            )
            //toast(contract.toString())
            performUpdateContract(contract)
//            if (renterID == -1) {
//                toast("Vui lòng thêm người thuê cho hợp đồng")
//            } else {
//
//            }
        }

    }

    private fun performUpdateContract(contract: ContractModel) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.updateContract(contract)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)
                        if (response == "Cập nhật hợp đồng thành công") {
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
    }/* END performDeleteRoom() */

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
}

override fun onDestroy() {
    super.onDestroy()
    disposable?.dispose()
}
}