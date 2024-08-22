package com.tranconghaoit.homemanager.activities.users

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityRegisterBinding
import com.tranconghaoit.homemanager.utils.NetworkUtils
import com.tranconghaoit.homemanager.utils.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupEvents()
    }

    private fun setupEvents() {
        binding.progressBar.visibility = View.GONE

        val version = 1.0
        binding.tvVersion.text = getString(R.string.app_version, version.toString())

        Glide.with(this)
            .load(R.drawable.image_register)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imgRegister)

        binding.btnRegister.setOnClickListener {
            val name = binding.edtFullName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val numberPhone = binding.edtNumberPhone.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (isInputValid(name, email, numberPhone, password)) {
                performRegister(name, email, numberPhone, password)
            }
        }
    }

    private fun isInputValid(
        name: String,
        email: String,
        numberPhone: String,
        password: String
    ): Boolean {
        if (name.isEmpty()) {
            toast("Vui lòng nhập họ tên")
            return false
        }
        if (email.isEmpty() || !Utils.checkEmail(email)) {
            toast("Email không đúng định dạng")
            return false
        }
        if (numberPhone.isEmpty()) {
            toast("Vui lòng nhập số điện thoại")
            return false
        }
        if (password.length < 5) {
            toast("Vui lòng nhập mật khẩu ít nhất 5 kí tự")
            return false
        }
        return true
    }

    private fun performRegister(
        name: String,
        email: String,
        numberPhone: String,
        password: String
    ) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.register(name, email, numberPhone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)
                        if (response == "Đăng ký thành công") {
                            binding.progressBar.visibility = View.VISIBLE

                            // Chuyển đến LoginActivity sau khi đăng ký
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
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
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}