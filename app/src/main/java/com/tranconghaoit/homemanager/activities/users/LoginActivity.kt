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
import com.tranconghaoit.homemanager.activities.MainActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityLoginBinding
import com.tranconghaoit.homemanager.models.UserModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import com.tranconghaoit.homemanager.utils.UserManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupEvents()
    }

    private fun setupEvents() {
        binding.progressBar.visibility = View.GONE

        val version = 1.0
        binding.tvVersion.text = getString(R.string.app_version, version.toString())

        Glide.with(this)
            .load(R.drawable.image_login)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imgLogin)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        val extras = intent.extras
        if (extras != null) {
            val email = extras.getString("email")
            if (!email.isNullOrEmpty()) {
                binding.edtEmail.setText(email)
            }
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (isInputValid(email, password)) {
                performLogin(email, password)
            }
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            toast("Email hoặc mật khẩu không đúng định dạng")
            return false
        }
        return true
    }

    private fun performLogin(email: String, password: String) {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserModel> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(user: UserModel) {
                        toast("Đăng nhập thành công")

                        // Lưu trạng thái đăng nhập
                        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.putInt("userID", user.userID)
                        editor.putString("name", user.name)
                        editor.putString("email", user.email)
                        editor.apply()

                        UserManager.setUser(user)

                        // Chuyển dữ liệu sang MainActivity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("userID", user.userID)
                        startActivity(intent)
                        finish()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        toast("Vui lòng kiểm tra lại email và mật khẩu")
                    }

                    override fun onComplete() {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                })
        } else {
            NetworkUtils.showToast(this)
        }
    }/* END performLogin(email: String, password: String) */

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
