package com.tranconghaoit.homemanager.activities.renters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityRenterAddBinding
import com.tranconghaoit.homemanager.models.RenterModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RenterAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRenterAddBinding
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRenterAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupEvents()
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

        binding.btnAdd.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val numberPhone = binding.edtNumberPhone2.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val birthday = DateFormatUtils.convertToMySQLDate(binding.edtBirthday.text.toString()).toString().trim()
            val citizenIDNumber = binding.edtCitizenIDNumber.text.toString().trim()
            val citizenIDIssueDate = DateFormatUtils.convertToMySQLDate(binding.edtCitizenIDIssueDate.text.toString()).toString().trim()
            val placeResidence = binding.edtPlaceResidence.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()

            if (isInputValid(name, numberPhone)) {
                val renter = RenterModel(
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
                )
                performAddRenter(renter)
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

    private fun performAddRenter(renter: RenterModel) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.addRenter(renter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        toast("Thêm người thuê thành công")

                        val renterID = responseBody.string()
                        val intent = Intent()
                        intent.putExtra("renterID", renterID.toInt())
                        setResult(Activity.RESULT_OK, intent)
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

    /*
        private fun getRenterIDByName(renterName: String) {
            if (NetworkUtils.haveNetworkConnection(this)) {
                RetrofitClient.apiService.getRenterIDByName(renterName) // Thực hiện truy vấn lấy RenterID dựa trên tên người thuê
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Int> {
                        override fun onSubscribe(d: Disposable) {
                            // Không cần làm gì ở đây
                        }

                        override fun onNext(renterID: Int) {
                            // renterID là giá trị RenterID lấy từ cơ sở dữ liệu
                            // Bây giờ bạn có thể tiếp tục và truyền renterID sang AddContractActivity
                            val intent = Intent(this@AddRenterActivity, AddContractActivity::class.java)
                            intent.putExtra("renterID", renterID)
                            startActivity(intent)
                        }

                        override fun onError(e: Throwable) {
                            toast("Không thể lấy RenterID từ cơ sở dữ liệu")
                        }

                        override fun onComplete() {
                            // Không cần làm gì ở đây
                        }
                    })
            } else {
                NetworkUtils.showToast(this)
            }
        }*//* END getRenterIDByName() */

    private fun convertToDate(rawDate: String): String {
        val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        try {
            val date = dateFormat.parse(rawDate)
            return formattedDate.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rawDate
    }

    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showCameraQR()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            toast("CAMERA permission requỉed")
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showCameraQR()
            }
        }
    private val scanLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    toast("Cancelled")
                } else {
                    setResult(result.contents)
                }
            }
        }

    private fun setResult(string: String) {
        val dataParts = string.split("|")

        if (dataParts.size >= 7) {
            val citizenIDNumber = dataParts[0]
            val name = dataParts[2]
            val birthday = convertToDate(dataParts[3])
            val address = dataParts[5]
            val citizenIDIssueDate = convertToDate(dataParts[6])
            // Hiển thị thông tin trên các EditText
            binding.edtName.setText(name)
            binding.edtBirthday.setText(birthday)
            binding.edtCitizenIDNumber.setText(citizenIDNumber)
            binding.edtCitizenIDIssueDate.setText(citizenIDIssueDate)
            binding.edtAddress.setText(address)
        }
    }

    private fun showCameraQR() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Thêm người thuê")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuQR -> {
                checkPermissionCamera(this)
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
}