package com.tranconghaoit.homemanager.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.bills.BillActivity
import com.tranconghaoit.homemanager.activities.contracts.ContractActivity
import com.tranconghaoit.homemanager.activities.renters.RenterActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentFirstBinding
import com.tranconghaoit.homemanager.models.CounterModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class FirstFragment : Fragment(R.layout.fragment_first) {
    private lateinit var binding: FragmentFirstBinding
    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)

        setupCounter()
        setupEvents()
    }

    private fun setupEvents() {
        binding.btnService.setOnClickListener {
            //startActivity(Intent(requireContext(), ServiceActivity::class.java))
            Toast.makeText(requireContext(), "Dịch vụ của bạn", Toast.LENGTH_SHORT).show()
        }
        binding.btnRenter.setOnClickListener {
            startActivity(Intent(requireContext(), RenterActivity::class.java))
        }
        binding.btnContract.setOnClickListener {
            startActivity(Intent(requireContext(), ContractActivity::class.java))
        }
        binding.btnBill.setOnClickListener {
            startActivity(Intent(requireContext(), BillActivity::class.java))
        }
    }

    private fun setupCounter() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val userID = requireActivity().intent.getIntExtra("userID", -1)
            if (userID > 0) {
                RetrofitClient.apiService.counter(userID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CounterModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(counter: CounterModel) {
                            with(binding) {
                                tvHomeNumber.text = counter.homeNumber.toString()
                                tvRenterNumber.text = counter.renterNumber.toString()
                                tvRoomNumber.text = counter.roomNumber.toString()
                                tvEmptyRoomNumber.text = counter.emptyRoomNumber.toString()
                            }

                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })
            }
        } else {
            NetworkUtils.showToast(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        setupCounter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }
}