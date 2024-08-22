package com.tranconghaoit.homemanager.fragments.bills

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.tranconghaoit.homemanager.adapters.BillAdapter
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentUnpaidBinding
import com.tranconghaoit.homemanager.models.BillModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale

class UnPaidFragment : Fragment() {
    private var _binding: FragmentUnpaidBinding? = null
    private val binding: FragmentUnpaidBinding get() = _binding!!
    private lateinit var mAdapter: BillAdapter
    private var originalList = ArrayList<BillModel>()
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentUnpaidBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()

        mAdapter = BillAdapter(requireContext(), arrayListOf())
        binding.rcvRoom.apply {
            layoutManager = GridLayoutManager(requireContext(), GridLayoutManager.VERTICAL)
            adapter = mAdapter
        }
        performGetBill()
    }

    private fun setupEvents() {
        binding.tvNull.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        binding.searchViewS.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterData(newText)
                return true
            }

        })
    }

    private fun performGetBill() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userID = sharedPrefs.getInt("userID", -1)
            RetrofitClient.apiService.getBillUnPaid(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<BillModel>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(billList: List<BillModel>) {
                        if (billList.isNotEmpty()) {
                            binding.tvNull.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE

                            mAdapter.updateData(billList)

                            // Lọc tìm kiếm
                            originalList.clear()
                            originalList.addAll(billList)
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
            NetworkUtils.showToast(requireContext())
        }
    } /* END performGetBill() */

    private fun showNoData() {
        binding.tvNull.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    fun filterData(query: String) {
        val filteredList = ArrayList<BillModel>()
        for (item in originalList) {
            val billID = item.billID.toString().lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            val roomName = item.roomName.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            if (billID || roomName) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            binding.tvNull.visibility = View.VISIBLE
        } else {
            binding.tvNull.visibility = View.GONE
        }
        mAdapter.updateData(filteredList)
    }

    override fun onResume() {
        super.onResume()
        performGetBill()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}