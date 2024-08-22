package com.tranconghaoit.homemanager.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.users.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)
        val userID = sharedPrefs.getInt("userID", -1)

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                if (isLoggedIn) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userID", userID)
                    startActivity(intent)

                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                finish()
            },
            3000
        )
    }
}