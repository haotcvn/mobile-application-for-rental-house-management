package com.tranconghaoit.homemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tranconghaoit.homemanager.adapters.RevenueAdapter
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentThirdBinding
import com.tranconghaoit.homemanager.models.RevenueModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding: FragmentThirdBinding get() = _binding!!
    private lateinit var mAdapter: RevenueAdapter
    private var disposable: Disposable? = null
    private var userID: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentThirdBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userID = requireActivity().intent.getIntExtra("userID", -1)
        setupEvents()

        mAdapter = RevenueAdapter(requireContext(), arrayListOf())
        binding.rcvRevenue.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = mAdapter
        }
        performGetRevenue()
    }

    private fun setupEvents() {
        binding.tvNull.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun performGetRevenue() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val userID = requireActivity().intent.getIntExtra("userID", -1)
            if (userID > 0) {
                RetrofitClient.apiService.getRevenue(userID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<List<RevenueModel>> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(list: List<RevenueModel>) {
                            if (list.isNotEmpty()) {
                                binding.progressBar.visibility = View.GONE
                                mAdapter.updateData(list)
                            } else {
                                showNoData()
                            }
                        }

                        override fun onError(e: Throwable) {
                            showNoData()
                        }

                        override fun onComplete() {

                        }
                    })
            } else {
                showNoData()
            }
        } else {
            NetworkUtils.showToast(requireContext())
        }
    }

    private fun showNoData() {
        binding.tvNull.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        performGetRevenue()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}