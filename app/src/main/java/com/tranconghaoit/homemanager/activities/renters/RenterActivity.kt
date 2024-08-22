package com.tranconghaoit.homemanager.activities.renters

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tranconghaoit.homemanager.adapters.ViewPager2Adapter
import com.tranconghaoit.homemanager.databinding.ActivityRenterBinding
import com.tranconghaoit.homemanager.fragments.renter.HasRoomFragment
import io.reactivex.rxjava3.disposables.Disposable

class RenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRenterBinding
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        //setupEvents()
        setupViewPager2()
    }

    private fun setupViewPager2() {
        val fragments = listOf(HasRoomFragment())
        val mAdapter = ViewPager2Adapter(supportFragmentManager, lifecycle, fragments)

        binding.viewPager2.adapter = mAdapter

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("ĐÃ CÓ PHÒNG"))
        //binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Chưa có phòng"))



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