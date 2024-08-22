package com.tranconghaoit.homemanager.activities

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.zxing.integration.android.IntentIntegrator
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.databinding.ActivityAddRepresentativeBinding

class AddRepresentativeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRepresentativeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRepresentativeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        //setupEvents()
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuQR -> { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
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
}