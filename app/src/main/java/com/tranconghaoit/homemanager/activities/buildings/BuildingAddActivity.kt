package com.tranconghaoit.homemanager.activities.buildings

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityBuildingAddBinding
import com.tranconghaoit.homemanager.models.BuildingModel
import com.tranconghaoit.homemanager.models.ProvinceModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import com.tranconghaoit.homemanager.utils.ProvinceDistrictSelector
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody

class BuildingAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuildingAddBinding
    private lateinit var provinceDistrictSelector: ProvinceDistrictSelector
    private var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildingAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        provinceDistrictSelector = ProvinceDistrictSelector(this)

        setupToolbar()
        setupEvents()
    }

    private fun setupEvents() {
        //val list = resources.getStringArray(R.array.province_city)
        //val mAdapter = ArrayAdapter(this@BuildingAddActivity, android.R.layout.simple_list_item_1, list)
        //binding.autoProvince.setAdapter(mAdapter)

        binding.edtProvince.setOnClickListener {
            performGetProvince()
        }

        binding.edtDistrict.setOnClickListener {
            performGetDistrict()
        }

        binding.btnHomeAdd.setOnClickListener {
            val name = binding.edtHomeName.text.toString().trim()
            val numberFloors = binding.edtNumberFloors.text.toString().trim()
            val priceText = binding.edtPrice.text.toString().trim()
            val describe = binding.edtDescribe.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()
            val province = binding.edtProvince.text.toString().trim()
            val district = binding.edtDistrict.text.toString().trim()
            val homeNote = binding.edtHomeNote.text.toString().trim()
            val billNote = binding.edtBillNote.text.toString().trim()

            if (isInputValid(name, numberFloors, describe, address, province, district)) {
                val userID = intent.getIntExtra("userID", -1)
                val price = if (priceText.isNotEmpty()) priceText.toInt() else 0

                val home = BuildingModel(
                    userID,
                    name,
                    numberFloors.toInt(),
                    price,
                    describe,
                    address,
                    province,
                    district,
                    homeNote,
                    billNote,
                    0
                )
                performAddHome(home)
            }
        }
    }

    private fun isInputValid(
        name: String, numberFloors: String, describe: String,
        address: String, province: String, district: String
    ): Boolean {
        val fields = listOf(
            name to "tên toà nhà",
            numberFloors to "số tầng",
            describe to "mô tả",
            address to "dịa chỉ nhà",
            province to "tỉnh",
            district to "quận",
        )
        for ((value, field) in fields) {
            if (value.isEmpty()) {
                toast("Vui lòng nhập $field")
                return false
            }
        }
        return true
    }

    private fun performAddHome(home: BuildingModel) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.addHome(home)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)

                        if (response == "Thêm toà nhà thành công") {
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
    }/* END performAddHome() */

    private fun performGetProvince() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.getProvince()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<ProvinceModel>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(list: List<ProvinceModel>) {
                        provinceDistrictSelector.isSelectedProvince = true
                        provinceDistrictSelector.showProvinceDistrict(list, binding.edtProvince)
                        binding.edtDistrict.setText("")
                    }

                    override fun onError(e: Throwable) {
                        toast("Không thể thực hiện")
                    }

                    override fun onComplete() {}
                })
        } else {
            NetworkUtils.showToast(this)
        }
    }/* END performGetProvince() */

    private fun performGetDistrict() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            val provinceID = provinceDistrictSelector.provinceID
            if (provinceID != "") {
                RetrofitClient.apiService.getDistrict(provinceID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<List<ProvinceModel>> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(list: List<ProvinceModel>) {
                            provinceDistrictSelector.isSelectedProvince = false
                            provinceDistrictSelector.showProvinceDistrict(list, binding.edtDistrict)
                        }

                        override fun onError(e: Throwable) {
                            toast("Không thể thực hiện")
                        }

                        override fun onComplete() {}
                    })
            }
        } else {
            NetworkUtils.showToast(this)
        }

    }/* END performGetDistrict() */

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