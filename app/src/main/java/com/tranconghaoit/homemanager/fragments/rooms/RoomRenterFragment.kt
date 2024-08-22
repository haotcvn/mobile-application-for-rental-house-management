package com.tranconghaoit.homemanager.fragments.rooms

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.tranconghaoit.homemanager.activities.contracts.ContractAddActivity
import com.tranconghaoit.homemanager.activities.renters.RenterAddActivity
import com.tranconghaoit.homemanager.adapters.RenterAdapter
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentRoomRenterBinding
import com.tranconghaoit.homemanager.models.RoomRenterModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RoomRenterFragment : Fragment() {
    private var _binding: FragmentRoomRenterBinding? = null
    private val binding: FragmentRoomRenterBinding get() = _binding!!
    private lateinit var mAdapter: RenterAdapter
    private var roomID: Int = -1
    private var hasData: Boolean = false
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentRoomRenterBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomID = requireActivity().intent.getIntExtra("roomID", -1)

        mAdapter = RenterAdapter(requireContext(), arrayListOf())
        binding.rcvRenter.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = mAdapter
        }
        setupEvents()
        performGetRenterInRoom()
    }

    private fun setupEvents() {
        binding.tvNull.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        binding.fab.setOnClickListener {
            if (!hasData) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.apply {
                    setMessage("Phòng chưa có hợp đồng, bạn cần tạo hợp đồng để thêm người thuê")
                    setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("Tạo hợp đồng") { dialogInterface: DialogInterface, i: Int ->
                        val intent = Intent(requireContext(), ContractAddActivity::class.java)
                        intent.putExtra("roomID", roomID)
                        startActivity(intent)
                    }
                    // Không cho đóng dialog khi click ra ngoài
                    setCancelable(false)
                }
                dialog.create()
                dialog.show()
            } else {
                startActivity(Intent(requireContext(), RenterAddActivity::class.java))
            }
        }
    }/* END setupEvents */

    private fun performGetRenterInRoom() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val roomID = requireActivity().intent.getIntExtra("roomID", 0)
            if (roomID > 0) {
                RetrofitClient.apiService.getRenterInRoom(roomID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<List<RoomRenterModel>> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(renter: List<RoomRenterModel>) {
                            if (renter != null) {
                                binding.progressBar.visibility = View.GONE
                                mAdapter.updateData(renter)

                                // Cập nhật biến hasData khi có dữ liệu
                                hasData = renter.isNotEmpty()
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
    }/* END performGetRoomRenter() */


    private fun showNoData() {
        binding.tvNull.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        performGetRenterInRoom()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}