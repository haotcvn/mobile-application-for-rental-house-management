package com.tranconghaoit.homemanager.fragments.renter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.tranconghaoit.homemanager.adapters.RenterAdapter
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentHasRoomBinding
import com.tranconghaoit.homemanager.models.RoomRenterModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale

class HasRoomFragment : Fragment() {
    private var _binding: FragmentHasRoomBinding? = null
    private val binding: FragmentHasRoomBinding get() = _binding!!
    private lateinit var mAdapter: RenterAdapter
    private var originalList = ArrayList<RoomRenterModel>()
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentHasRoomBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
        mAdapter = RenterAdapter(requireContext(), arrayListOf())
        binding.rcvRenter.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = mAdapter
        }
        performGetRenterHasRoom()
    }

    private fun setupEvents() {
        binding.tvNull.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText.toString())
                return true
            }
        })
    }

    fun filterData(query: String) {
        val filteredList = ArrayList<RoomRenterModel>()
        for (item in originalList) {
            val billID = item.renterName.lowercase(Locale.ROOT).contains(
                query.lowercase(
                    Locale.ROOT
                )
            )
            val numberPhone =
                item.numberPhone.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            val roomName =
                item.roomName.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))

            if (billID || numberPhone || roomName) {
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

    private fun performGetRenterHasRoom() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userID = sharedPrefs.getInt("userID", -1)
            RetrofitClient.apiService.getRenterHasRoom(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<RoomRenterModel>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(list: List<RoomRenterModel>) {
                        binding.progressBar.visibility = View.GONE
                        mAdapter.updateData(list)

                        // Lọc tìm kiếm
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
        performGetRenterHasRoom()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}