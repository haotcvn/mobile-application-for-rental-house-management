package com.tranconghaoit.homemanager.activities.renters

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityRenterUpdateBinding
import com.tranconghaoit.homemanager.models.RenterModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.util.Calendar

class RenterUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRenterUpdateBinding
    private var disposable: Disposable? = null
    private lateinit var renter: RenterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRenterUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupEvents()

        renter = intent.getSerializableExtra("renter") as RenterModel
        showInformation()
    }

    @SuppressLint("SetTextI18n")
    private fun setupEvents() {
        val toDay = Calendar.getInstance()
        val startDay = toDay.get(Calendar.DAY_OF_MONTH)
        val startMonth = toDay.get(Calendar.MONTH)
        val startYear = toDay.get(Calendar.YEAR)
        binding.edtBirthday.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val formattedDay = String.format("%02d", dayOfMonth)
                    val formattedMonth = String.format("%02d", month + 1)
                    binding.edtBirthday.setText("$formattedDay-${formattedMonth}-$year")
                },
                startYear,
                startMonth,
                startDay
            ).show()
        }

        binding.edtCitizenIDIssueDate.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val formattedDay = String.format("%02d", dayOfMonth)
                    val formattedMonth = String.format("%02d", month + 1)
                    binding.edtCitizenIDIssueDate.setText("$formattedDay-${formattedMonth}-$year")
                },
                startYear,
                startMonth,
                startDay
            ).show()
        }

        binding.btnUpdate.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val numberPhone = binding.edtNumberPhone.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val birthday = DateFormatUtils.convertToMySQLDate(binding.edtBirthday.text.toString().trim()).toString()
            val citizenIDNumber = binding.edtCitizenIDNumber.text.toString().trim()
            val citizenIDIssueDate = DateFormatUtils.convertToMySQLDate(binding.edtCitizenIDIssueDate.text.toString().trim()).toString()
            val placeResidence = binding.edtPlaceResidence.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()

            if (isInputValid(name, numberPhone)) {
                val renters = RenterModel(
                    renter.renterID,
                    name,
                    numberPhone,
                    email,
                    birthday,
                    citizenIDNumber,
                    citizenIDIssueDate,
                    placeResidence,
                    address,
                    null,
                    null,
                    null,
                    0
                )
                //toast(renters.toString())
                performUpdateRenter(renters)
            }
        }
    }

    private fun isInputValid(name: String, numberPhone: String): Boolean {
        val fields = listOf(
            name to "tên người thuê",
            numberPhone to "số điện thoại"
        )
        for ((value, field) in fields) {
            if (value.isEmpty()) {
                toast("Vui lòng nhập $field")
                return false
            }
        }
        return true
    }

    private fun performUpdateRenter(renter: RenterModel) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.updateRenter(renter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)

                        if (response == "Cập nhật người thuê thành công")
                            finish()
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
    }/* END performAddRenter() */

    private fun showInformation() {
        binding.apply {
            edtName.setText(renter.name)
            edtNumberPhone.setText(renter.numberPhone)
            edtEmail.setText(renter.email)
            edtBirthday.setText(DateFormatUtils.convertDateFormat(renter.birthday))
            edtCitizenIDNumber.setText(renter.citizenIDNumber)
            edtCitizenIDIssueDate.setText(DateFormatUtils.convertDateFormat(renter.citizenIDIssueDate))
            edtPlaceResidence.setText(renter.placeResidence)
            edtAddress.setText(renter.address)
        }
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuQR -> {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                val qrScan = IntentIntegrator(this)
                qrScan.setOrientationLocked(false) // Cho phép xoay màn hình
                qrScan.setPrompt("Thêm khách thuê")
                qrScan.setBeepEnabled(false)
                qrScan.initiateScan()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_add, menu)
        return true
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