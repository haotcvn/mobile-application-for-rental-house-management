package com.tranconghaoit.homemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tranconghaoit.homemanager.adapters.ViewPager2Adapter
import com.tranconghaoit.homemanager.databinding.FragmentFourBinding
import io.reactivex.rxjava3.disposables.Disposable

class FourFragment : Fragment() {
    private var _binding: FragmentFourBinding? = null
    private val binding: FragmentFourBinding get() = _binding!!
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFourBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = listOf(NotificationSystemFragment(), NotificationPromotionFragment())
        val mAdapter = ViewPager2Adapter(requireActivity().supportFragmentManager, lifecycle, fragments)
        binding.viewPager2.adapter = mAdapter

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("HỆ THỐNG"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("KHUYẾN MÃI"))

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}