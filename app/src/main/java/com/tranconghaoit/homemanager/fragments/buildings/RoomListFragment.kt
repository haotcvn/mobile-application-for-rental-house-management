package com.tranconghaoit.homemanager.fragments.buildings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.rooms.RoomAddActivity
import com.tranconghaoit.homemanager.adapters.RoomAdapter
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentRoomListBinding
import com.tranconghaoit.homemanager.models.RoomModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale

class RoomListFragment : Fragment(R.layout.fragment_room_list) {
    private lateinit var binding: FragmentRoomListBinding
    private lateinit var mAdapter: RoomAdapter
    private var originalList = ArrayList<RoomModel>()
    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRoomListBinding.bind(view)

        setupEvents()

        mAdapter = RoomAdapter(requireContext(), arrayListOf())
        binding.rcvRoom.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter
        }
        performGetRoom()
    }

    private fun setupEvents() {
        binding.tvNull.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        binding.fab.setOnClickListener {
            startActivity(Intent(requireContext(), RoomAddActivity::class.java))
        }

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

    private fun performGetRoom() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val homeID = requireActivity().intent.getIntExtra("homeID", 0)
            if (homeID > 0) {
                RetrofitClient.apiService.getRoom(homeID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<List<RoomModel>> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(roomList: List<RoomModel>) {
                            if (roomList.isNotEmpty()) {
                                binding.progressBar.visibility = View.GONE
                                mAdapter.updateData(roomList)

                                // Lọc tìm kiếm
                                originalList.clear()
                                originalList.addAll(roomList)
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
    }/* END performGetRoom() */

    private fun showNoData() {
        binding.tvNull.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    fun filterData(query: String) {
        val filteredList = ArrayList<RoomModel>()
        for (item in originalList) {
            if (item.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))) {
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
        performGetRoom()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
