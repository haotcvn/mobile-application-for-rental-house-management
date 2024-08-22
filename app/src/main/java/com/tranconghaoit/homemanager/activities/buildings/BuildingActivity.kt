package com.tranconghaoit.homemanager.activities.buildings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.adapters.ViewPager2Adapter
import com.tranconghaoit.homemanager.databinding.ActivityBuildingBinding
import com.tranconghaoit.homemanager.fragments.buildings.BuildingDetailedFragment
import com.tranconghaoit.homemanager.fragments.buildings.RoomListFragment
import com.tranconghaoit.homemanager.utils.HomeManager

class BuildingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuildingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewPager2()
    }

    private fun setupViewPager2() {
        val fragments = listOf(RoomListFragment(), BuildingDetailedFragment())
        val mAdapter = ViewPager2Adapter(supportFragmentManager, lifecycle, fragments)
        binding.viewPager2.adapter = mAdapter

        val tabTexts = listOf("DANH SÁCH PHÒNG", "CHI TIẾT")
        binding.tabLayout.apply { tabTexts.forEach { tabText ->
                addTab(newTab().setText(tabText))
            }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    binding.viewPager2.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuEditHome -> {
                startActivity(Intent(this, BuildingUpdateActivity::class.java))
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = HomeManager.getHome().name
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}