package com.tranconghaoit.homemanager.activities.renters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityRenterDetailedBinding
import com.tranconghaoit.homemanager.models.RenterModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody

class RenterDetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRenterDetailedBinding
    private var disposable: Disposable? = null
    private var renterID: Int = -1
    private lateinit var renter: RenterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRenterDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupEvents()

        renterID = intent.getIntExtra("renterID", -1)

        performGetRenter()
    }

    private fun setupEvents() {
        binding.progressBar.visibility = View.GONE

        binding.btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setMessage("Bạn có chắc chắn muốn xoá người thuê?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                    performDeleteRenter()
                }
            }
            dialog.create()
            dialog.show()
        }
    }/* END setupEvents() */

    private fun performDeleteRenter() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (renterID > 0) {
                RetrofitClient.apiService.deleteRenter(renterID)
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

                            if (response == "Xoá người thuê thành công"){
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

    private fun performGetRenter() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            if (renterID > 0) {
                RetrofitClient.apiService.getRenterDetailed(renterID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<RenterModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNext(obj: RenterModel) {
                            binding.apply {
                                renter = obj
                                progressBar.visibility = View.GONE

                                tvName.text = obj.name
                                tvEmail.text = obj.email
                                tvBirthday.text = DateFormatUtils.convertDateFormat(obj.birthday)
                                tvNumberPhone.text = obj.numberPhone
                                val roomName = intent.getStringExtra("roomName")
                                tvRoom.text = roomName
                                tvCitizenIDNumber.text = obj.citizenIDNumber
                                tvCitizenIDIssueDate.text = DateFormatUtils.convertDateFormat(obj.citizenIDIssueDate)
                                tvPlaceResidence.text = obj.placeResidence
                                tvAddress.text = obj.address
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuEditRoom -> {
                val intent = Intent(this, RenterUpdateActivity::class.java)
                intent.putExtra("renter", renter)
                startActivity(intent)
            }
        }
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_room, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        performGetRenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}