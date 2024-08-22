package com.tranconghaoit.homemanager.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tranconghaoit.homemanager.activities.users.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)
        val userID = sharedPrefs.getInt("userID", -1)

        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)

        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}