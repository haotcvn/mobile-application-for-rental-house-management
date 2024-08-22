package com.tranconghaoit.homemanager.activities.bills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tranconghaoit.homemanager.databinding.ActivityBillBinding
import com.tranconghaoit.homemanager.adapters.ViewPager2Adapter
import com.tranconghaoit.homemanager.fragments.bills.PaidFragment
import com.tranconghaoit.homemanager.fragments.bills.UnPaidFragment

class BillActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupViewPager2()
        setupEvents()
    }

    private fun setupEvents() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, BillAddActivity::class.java))
        }
    }

    private fun setupViewPager2() {
        val fragments = listOf(UnPaidFragment(), PaidFragment())
        val mAdapter = ViewPager2Adapter(supportFragmentManager, lifecycle, fragments)
        binding.viewPager2.adapter = mAdapter

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("CHƯA THANH TOÁN"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("ĐÃ THANH TOÁN"))



        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}