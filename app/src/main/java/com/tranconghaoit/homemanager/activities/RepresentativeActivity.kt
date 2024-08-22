package com.tranconghaoit.homemanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.databinding.ActivityRepresentativeBinding

class RepresentativeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepresentativeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepresentativeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupEvents()
    }

    private fun setupEvents() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddRepresentativeActivity::class.java))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}