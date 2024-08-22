package com.tranconghaoit.homemanager.activities.users

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.MainActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityChangePasswordBinding
import com.tranconghaoit.homemanager.models.UserModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import com.tranconghaoit.homemanager.utils.UserManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupEvents()
    }

    private fun setupEvents() {
        binding.progressBar.visibility = View.GONE

        val version = 1.0
        binding.tvVersion.text = getString(R.string.app_version, version.toString())

        binding.btnUpdate.setOnClickListener {
            val userID = intent.getIntExtra("userID", -1)
            val passwordOld = binding.edtPasswordOld.text.toString().trim()
            val passwordNew = binding.edtPasswordNew.text.toString().trim()

            if (isInputValid(passwordOld, passwordNew)) {
                performUpdate(userID, passwordOld, passwordNew)
            }
        }
    }

    private fun performUpdate(userID: Int, passwordOld: String, passwordNew: String) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.changePassword(userID, passwordOld, passwordNew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)
                        if (response == "Thay đổi mật khẩu thành công")
                            finish()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                })
        } else {
            NetworkUtils.showToast(this)
        }
    }

    private fun isInputValid(passwordOld: String, passwordNew: String): Boolean {
        if (passwordOld.isEmpty()) {
            toast("Mật khẩu hiện tại không được để trống")
            return false
        }
        if (passwordNew.length < 5) {
            toast("Vui lòng nhập mật khẩu ít nhất 5 kí tự")
            return false
        }
        return true
    }

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