package com.tranconghaoit.homemanager.activities.rooms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.bills.BillActivity
import com.tranconghaoit.homemanager.adapters.ViewPager2Adapter
import com.tranconghaoit.homemanager.databinding.ActivityRoomBinding
import com.tranconghaoit.homemanager.fragments.rooms.RoomDetailedFragment
import com.tranconghaoit.homemanager.fragments.rooms.RoomRenterFragment
import com.tranconghaoit.homemanager.utils.RoomManager

class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupViewPager2()
    }

    private fun setupViewPager2() {
        val fragments = listOf(RoomDetailedFragment(), RoomRenterFragment())
        val mAdapter = ViewPager2Adapter(supportFragmentManager, lifecycle, fragments)
        binding.viewPager2.adapter = mAdapter

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("THÔNG TIN"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("NGƯỜI THUÊ"))

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuEditRoom -> {
                startActivity(Intent(this, RoomUpdateActivity::class.java))
            }
            R.id.menuPayment -> {
                startActivity(Intent(this, BillActivity::class.java))
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_room, menu)
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = RoomManager.getRoom().name
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}