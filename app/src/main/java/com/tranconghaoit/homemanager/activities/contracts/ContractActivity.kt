package com.tranconghaoit.homemanager.activities.contracts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tranconghaoit.homemanager.adapters.ViewPager2Adapter
import com.tranconghaoit.homemanager.databinding.ActivityContractBinding
import com.tranconghaoit.homemanager.fragments.contracts.ActiveFragment
import io.reactivex.rxjava3.disposables.Disposable

class ContractActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContractBinding
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        //setupEvents()
        setupViewPager2()
    }

    private fun setupViewPager2() {
        val fragments = listOf(ActiveFragment())
        val mAdapter = ViewPager2Adapter(supportFragmentManager, lifecycle, fragments)

        binding.viewPager2.adapter = mAdapter

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("ĐANG HOAT ĐỘNG"))
       // binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Quá hạn"))
        //binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đã thanh lý"))

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