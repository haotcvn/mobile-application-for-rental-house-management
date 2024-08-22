@file:Suppress("DEPRECATION")

package com.tranconghaoit.homemanager.activities.contracts

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import com.tranconghaoit.homemanager.activities.renters.RenterAddActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityContractAddBinding
import com.tranconghaoit.homemanager.models.ContractModel
import com.tranconghaoit.homemanager.models.RenterModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.HomeManager
import com.tranconghaoit.homemanager.utils.NetworkUtils
import com.tranconghaoit.homemanager.utils.RoomManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.util.Calendar

class ContractAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContractAddBinding
    private var roomID: Int = -1
    private var renterID: Int = -1
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomID = intent.getIntExtra("roomID", -1)

        setupToolbar()
        setupEvents()
        showInformation()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            renterID = data?.getIntExtra("renterID", -1)!!
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupEvents() {
        binding.cartRenter.visibility = View.GONE
        binding.tvNull.visibility = View.VISIBLE

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

        binding.btnRenter.setOnClickListener {
            val intent = Intent(this, RenterAddActivity::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.btnContractAdd.setOnClickListener {
            val roomID = RoomManager.getRoom().roomID
            val startDate = DateFormatUtils.convertToMySQLDate(binding.edtStartDay).toString()
            val endDate = DateFormatUtils.convertToMySQLDate(binding.edtEndDay).toString()
            val duration = binding.edtDuration.text.toString().trim()
            val priceText = binding.edtPrice.text.toString().trim()
            val depositText = binding.edtDeposits.text.toString().trim()

            val price = if (priceText.isNotEmpty()) priceText.toInt() else 0
            val deposit = if (depositText.isNotEmpty()) depositText.toInt() else 0

            val contract = ContractModel(null, renterID, roomID, startDate, endDate, duration.toInt(), price, deposit, 0)

            if (renterID == -1) {
                toast("Vui lòng thêm người thuê cho hợp đồng")
            } else {
                performAddContract(contract)
            }
        }
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun showInformation() {
        val home = HomeManager.getHome()
        val room = RoomManager.getRoom()
        with(binding) {
            edtRoom.setText("${room.name} - ${home.name}")
            edtPrice.setText("${room.price}")
            edtDeposits.setText("${room.deposits}")
            edtStartDay.setText(DateFormatUtils.getCurrentDateAsString())
            edtPriceDateStart.setText(DateFormatUtils.getCurrentDateAsString())

        }
    }

    private fun performAddContract(contract: ContractModel) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.addContract(contract)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)

                        if (response == "Tạo hợp đồng thành công") {
                            finish()
                        }
                    }

                    override fun onError(e: Throwable) {
                        toast("Không thể thực hiện")
                    }

                    override fun onComplete() {

                    }
                })
        } else {
            NetworkUtils.showToast(this)
        }
    }/* END performAddContract() */

    private fun performGetRenterInContract() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (renterID > 0) {
                RetrofitClient.apiService.getRenterInContract(renterID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<RenterModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNext(renter: RenterModel) {
                            binding.tvRenterName.text = renter.name
                            binding.tvNumberPhone.text = renter.numberPhone

                            binding.cartRenter.visibility = View.VISIBLE
                            binding.tvNull.visibility = View.GONE
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
    }/* END performGetRenterInContract */

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        performGetRenterInContract()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}