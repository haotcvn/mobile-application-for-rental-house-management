package com.tranconghaoit.homemanager.activities.rooms

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityRoomUpdateBinding
import com.tranconghaoit.homemanager.models.RoomModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import com.tranconghaoit.homemanager.utils.RoomManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody

class RoomUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomUpdateBinding
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupEvents()
        showInformation()
    }

    private fun setupEvents() {
        binding.progressBar.visibility = View.GONE

        binding.btnRoomUpdate.setOnClickListener {
            val name = binding.edtRoomName.text.toString().trim()
            val priceText = binding.edtPrice.text.toString().trim()
            val floors = binding.edtFloors.text.toString().trim()
            val numberBedroom = binding.edtnumberBedroom.text.toString().trim()
            val numberLivingroom = binding.edtnumberLivingroom.text.toString().toString()
            val acreage = binding.edtAcreage.text.toString().trim()
            val numberPeopleLimit = binding.edtNumberPeopleLimit.text.toString().trim()
            val depositsText = binding.edtDeposits.text.toString().trim()
            val gender = if (binding.rdoMale.isChecked) binding.rdoMale.text.toString() else binding.rdoFemale.text.toString()
            val describe = binding.edtDescribe.text.toString().trim()
            val note = binding.edtNote.text.toString().trim()

            if (isInputValid(name, priceText, floors, numberBedroom, numberLivingroom, acreage, numberPeopleLimit)) {
                val roomID = RoomManager.getRoom().roomID
                val price = if (priceText.isNotEmpty()) priceText.toInt() else 0
                val deposits = if (depositsText.isNotEmpty()) depositsText.toInt() else 0

                val room = RoomModel(
                    roomID,
                    null,
                    name,
                    price,
                    floors.toInt(),
                    numberBedroom.toInt(),
                    numberLivingroom.toInt(),
                    acreage.toInt(),
                    numberPeopleLimit.toInt(),
                    deposits, // Tiền đặt cọc
                    gender,
                    describe,
                    note,
                    0
                )
                performUpdateRoom(room)
            }
        }
    }/* END setupEvents() */



    private fun performUpdateRoom(room: RoomModel) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.updateRoom(room)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)

                        if (response == "Cập nhật phòng thành công") {
                            binding.progressBar.visibility = View.VISIBLE
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
    }/* END performUpdateRoom() */

    private fun isInputValid(
        name: String,
        price: String,
        floors: String,
        numberBedroom: String,
        numberLivingroom: String,
        acreage: String,
        numberPeopleLimit: String
    ): Boolean {
        val fields = listOf(
            name to "tên toà nhà",
            price to "giá dự kiến",
            floors to "tầng",
            numberBedroom to "số phòng ngủ",
            numberLivingroom to "số phòng khách",
            acreage to "diện tích",
            numberPeopleLimit to "số người thuê"
        )
        for ((value, field) in fields) {
            if (value.isEmpty()) {
                toast("Vui lòng nhập $field")
                return false
            }
        }
        return true
    }

    private fun showInformation() {
        val room = RoomManager.getRoom()
        with(binding) {
            edtRoomName.setText(room.name)
            edtPrice.setText(room.price.toString())
            edtFloors.setText(room.floors.toString())
            edtnumberBedroom.setText(room.numberBedroom.toString())
            edtnumberLivingroom.setText(room.numberLivingroom.toString())
            edtAcreage.setText(room.acreage.toString())
            edtNumberPeopleLimit.setText(room.numberPeopleLimit.toString())
            edtDeposits.setText(room.deposits.toString())
            edtDescribe.setText(room.describe)
            edtNote.setText(room.note)

        }
    }/* END showInformation() */

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