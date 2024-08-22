package com.tranconghaoit.homemanager.fragments.buildings

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentBuildingDetailedBinding
import com.tranconghaoit.homemanager.models.BuildingModel
import com.tranconghaoit.homemanager.utils.HomeManager
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.text.DecimalFormat

class BuildingDetailedFragment : Fragment() {
    private var _binding: FragmentBuildingDetailedBinding? = null
    private val binding: FragmentBuildingDetailedBinding get() = _binding!!
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentBuildingDetailedBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        performGetHomeDetailed()
        setupEvents()
    }

    private fun setupEvents() {
        binding.btnHomeDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.apply {
                setTitle("Xác nhận")
                setMessage("Dữ liệu toà nhà ảnh đến thông tin hoá đơn và thống kê, khi xoá sẽ không khôi phục lại được. Bạn chắc chắn xoá?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                    performDeleteHome()
                }

            }
            dialog.create()
            dialog.show()
        }
    }/* END setupEvents() */

    private fun performDeleteHome() {
        val homeID = HomeManager.getHome().homeID
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            RetrofitClient.apiService.deleteHome(homeID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)
                        if (response == "Xoá toà nhà thành công") {
                            requireActivity().finish()
                        }
                    }

                    override fun onError(e: Throwable) {
                        toast("Không thể thực hiện")
                    }

                    override fun onComplete() {

                    }
                })
        } else {
            NetworkUtils.showToast(requireContext())
        }
    }/* END performDeleteHome() */

    private fun performGetHomeDetailed() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val homeID = requireActivity().intent.getIntExtra("homeID", 0)
            if (homeID > 0) {
                RetrofitClient.apiService.getHomeDetailed(homeID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BuildingModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNext(home: BuildingModel) {
                            with(binding) {
                                tvAddressDetailed.text =
                                    "${home.address}, ${home.district}, ${home.province}"
                                tvRoomNumber.text = home.roomNumber.toString()
                                tvRenterNumber.text = home.renterNumber.toString()
                                tvFloorsNumber.text = home.numberFloors.toString()
                                tvPrice.text =
                                    "${DecimalFormat("###,###,###").format(home.price)} đ"
                                tvUserName.text = home.userName
                                edtDescribe.setText(home.describe)
                                edtHomeNote.setText(home.homeNote)
                                edtBillNote.setText(home.billNote)

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
    }/* END performGetHomeDetailed() */

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        performGetHomeDetailed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}