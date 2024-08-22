package com.tranconghaoit.homemanager.fragments.contracts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tranconghaoit.homemanager.adapters.ContractAdapter
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentActiveBinding
import com.tranconghaoit.homemanager.models.ContractDetailedModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale

class ActiveFragment : Fragment() {
    private var _binding: FragmentActiveBinding? = null
    private val binding: FragmentActiveBinding get() = _binding!!
    private lateinit var mAdapter: ContractAdapter
    private var originalList = ArrayList<ContractDetailedModel>()
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentActiveBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
        mAdapter = ContractAdapter(requireContext(), arrayListOf())
        binding.rcvContract.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = mAdapter
        }
        performGetContract()
    }

    private fun setupEvents() {
        binding.tvNull.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterData(newText)
                return true
            }
        })
    }

    fun filterData(query: String) {
        val filteredList = ArrayList<ContractDetailedModel>()
        for (item in originalList) {
            val contractID = item.contractID.toString().lowercase(Locale.ROOT).contains(
                query.lowercase(
                    Locale.ROOT
            )
            )
            val roomName = item.roomName.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))

            if (contractID || roomName) {
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

    private fun performGetContract() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userID = sharedPrefs.getInt("userID", -1)
            RetrofitClient.apiService.getContract(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<ContractDetailedModel>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(list: List<ContractDetailedModel>) {
                        binding.progressBar.visibility = View.GONE
                        mAdapter.updateData(list)

                        originalList.clear()
                        originalList.addAll(list)
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
    }/* END performGetRoomRenter() */

    private fun showNoData() {
        binding.tvNull.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        performGetContract()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}