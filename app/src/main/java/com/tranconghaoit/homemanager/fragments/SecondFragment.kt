package com.tranconghaoit.homemanager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tranconghaoit.homemanager.activities.buildings.BuildingAddActivity
import com.tranconghaoit.homemanager.adapters.BuildingAdapter
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentSecondBinding
import com.tranconghaoit.homemanager.models.BuildingModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding: FragmentSecondBinding get() = _binding!!
    private lateinit var mAdapter: BuildingAdapter
    private var disposable: Disposable? = null
    private var userID: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSecondBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userID = requireActivity().intent.getIntExtra("userID", -1)
        setupEvents()

        mAdapter = BuildingAdapter(requireContext(), arrayListOf())
        binding.rcvHome.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter
        }
        performGetHome()
    }

    private fun setupEvents() {
        binding.tvNull.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), BuildingAddActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
    }

    private fun performGetHome() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val userID = requireActivity().intent.getIntExtra("userID", -1)
            if (userID > 0) {
                RetrofitClient.apiService.getHome(userID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<List<BuildingModel>> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(list: List<BuildingModel>) {
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
        performGetHome()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}
