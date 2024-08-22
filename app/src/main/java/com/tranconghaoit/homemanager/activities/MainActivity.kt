package com.tranconghaoit.homemanager.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.users.ChangePasswordActivity
import com.tranconghaoit.homemanager.activities.users.LoginActivity
import com.tranconghaoit.homemanager.databinding.ActivityMainBinding
import com.tranconghaoit.homemanager.fragments.FirstFragment
import com.tranconghaoit.homemanager.fragments.FiveFragment
import com.tranconghaoit.homemanager.fragments.FourFragment
import com.tranconghaoit.homemanager.fragments.SecondFragment
import com.tranconghaoit.homemanager.fragments.ThirdFragment

private const val FRAGMENT_FIRST = 0
private const val FRAGMENT_SECOND = 1
private const val FRAGMENT_THIRD = 2
private const val FRAGMENT_FOUR = 3
private const val FRAGMENT_FIVE = 4

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mCurrentFragment = FRAGMENT_FIRST
    private var nightMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(FirstFragment())
        setupToolBar()
        setupHeader()
        setupDarkMode()
        setupEvents()

    }/* END onCreate */

    private fun setupEvents() {
        itemNavigationClick()
        itemNavigationBottomClick()
    }

    private fun setupHeader() {
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val names = sharedPrefs.getString("name", toString())
        val emails = sharedPrefs.getString("email", toString())

        val headerView = binding.navLeft.getHeaderView(0)
        val name = headerView.findViewById<TextView>(R.id.tvUserNameNav)
        val email = headerView.findViewById<TextView>(R.id.tvEmailNav)
        name.text = names
        email.text = emails
    }

    private fun setupDarkMode() {
        val menuItem = binding.navLeft.menu
        val switchMenuItem = menuItem.findItem(R.id.nav_left_dark_mode)

        val sharedPrefMode = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        nightMode = sharedPrefMode.getBoolean("nightMode", false)

        switchMenuItem?.let {
            val switchDarkMode: SwitchCompat = it.actionView?.findViewById(R.id.mSwitch) as SwitchCompat
            if (nightMode) {
                switchDarkMode.isChecked = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            switchDarkMode.setOnClickListener {
                val editor = sharedPrefMode.edit()
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.putBoolean("nightMode", false)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.putBoolean("nightMode", true)
                }
                editor.apply()
            }
        }
    }

    private fun itemNavigationBottomClick() {
        binding.navBottom.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_bottom_first -> {
                    openFirstFragment()
                    supportActionBar?.title = it.title.toString()
                }

                R.id.nav_bottom_second -> {
                    openSecondFragment()
                    supportActionBar?.title = it.title.toString()
                }

                R.id.nav_bottom_third -> {
                    openThirdFragment()
                    supportActionBar?.title = it.title.toString()
                }

                R.id.nav_bottom_four -> {
                    openFourFragment()
                    supportActionBar?.title = it.title.toString()
                }

                R.id.nav_bottom_five -> {
                    openFiveFragment()
                    supportActionBar?.title = it.title.toString()
                }
            }
            true
        }
    }/* END itemNavigationBottomClick */

    private fun itemNavigationClick() {
        with(binding) {
            navLeft.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_left_home -> {
                        openFirstFragment()
                        navBottom.menu.findItem(R.id.nav_bottom_first).isChecked = true
                        supportActionBar?.title = item.title.toString()
                    }

                    R.id.nav_left_notifications -> {
                        openFourFragment()
                        navBottom.menu.findItem(R.id.nav_bottom_four).isChecked = true
                        supportActionBar?.title = item.title.toString()
                    }

                    R.id.nav_left_statistical -> {
                        openThirdFragment()
                        navBottom.menu.findItem(R.id.nav_bottom_third).isChecked = true
                        supportActionBar?.title = item.title.toString()
                    }

                    R.id.nav_left_change_password -> {
                        val userID = intent.getIntExtra("userID", -1)
                        val intent = Intent(this@MainActivity, ChangePasswordActivity::class.java)
                        intent.putExtra("userID", userID)
                        startActivity(intent)
                    }

                    R.id.nav_left_logout -> {
                        val dialog = AlertDialog.Builder(this@MainActivity)
                        dialog.apply {
                            setMessage("Đăng xuất khoải tài khoản của bạn?")
                            setCancelable(false)
                            setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                                dialogInterface.dismiss()
                            }
                            setPositiveButton("Đăng xuất") { dialogInterface: DialogInterface, i: Int ->
//                                finish()
//                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                logout()
                            }

                        }
                        dialog.create()
                        dialog.show()

                    }
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }
    }/* END itemNavigationClick */

    private fun openFirstFragment() {
        if (mCurrentFragment != FRAGMENT_FIRST) {
            replaceFragment(FirstFragment())
            mCurrentFragment = FRAGMENT_FIRST
        }
    }

    private fun openSecondFragment() {
        if (mCurrentFragment != FRAGMENT_SECOND) {
            replaceFragment(SecondFragment())
            mCurrentFragment = FRAGMENT_SECOND
        }
    }

    private fun openThirdFragment() {
        if (mCurrentFragment != FRAGMENT_THIRD) {
            replaceFragment(ThirdFragment())
            mCurrentFragment = FRAGMENT_THIRD
        }
    }

    private fun openFourFragment() {
        if (mCurrentFragment != FRAGMENT_FOUR) {
            replaceFragment(FourFragment())
            mCurrentFragment = FRAGMENT_FOUR
        }
    }

    private fun openFiveFragment() {
        if (mCurrentFragment != FRAGMENT_FIVE) {

            replaceFragment(FiveFragment())
            mCurrentFragment = FRAGMENT_FIVE
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutContent, fragment)
            addToBackStack(null)
            commit()
            //invalidateOptionsMenu() // Cập nhật menu cho Fragment mới
        }
    }

    private fun setupToolBar() {
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }


    private fun setToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.menuCart -> {
//                startActivity(Intent(this, MyCartActivity::class.java))
//            }
//        }
        return true
    }

    private fun logout() {
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
        editor.clear()

        // Chuyển người dùng đến màn hình đăng nhập (LoginActivity) sau khi đăng xuất
        val email = sharedPrefs.getString("email", "")
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Tạo đối tượng builder
            val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.apply {
                setMessage("Bạn có muốn thoát ứng dụng không?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                    finish()
                }
            }
            dialog.show()
        }
    }/* END onBackPressed */
}