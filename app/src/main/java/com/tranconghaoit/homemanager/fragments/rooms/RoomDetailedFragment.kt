package com.tranconghaoit.homemanager.fragments.rooms

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.activities.contracts.ContractDetailedActivity
import com.tranconghaoit.homemanager.activities.contracts.ContractUpdateActivity
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databases.BillDAO
import com.tranconghaoit.homemanager.databinding.FragmentRoomDetailedBinding
import com.tranconghaoit.homemanager.models.BillModel
import com.tranconghaoit.homemanager.models.ContractDetailedModel
import com.tranconghaoit.homemanager.models.RoomModel
import com.tranconghaoit.homemanager.utils.DateFormatUtils
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.text.DecimalFormat


class RoomDetailedFragment : Fragment() {
    private var _binding: FragmentRoomDetailedBinding? = null
    private val binding: FragmentRoomDetailedBinding get() = _binding!!
    private var contractID: Int = -1
    private var roomID: Int = -1
    private lateinit var contract: ContractDetailedModel
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentRoomDetailedBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomID = requireActivity().intent.getIntExtra("roomID", -1)
        performGetRoomDetailed()
        performGetContract()
        setupEvents()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupEvents() {
        binding.cartViewContract.visibility = View.GONE

        binding.ContractClick.setOnClickListener {
            val intent = Intent(requireContext(), ContractDetailedActivity::class.java)
            intent.putExtra("contractID", contractID)
            startActivity(intent)
        }

        binding.btnContractEdit.setOnClickListener {
            val intent = Intent(requireContext(), ContractUpdateActivity::class.java)
            intent.putExtra("contract", contract)
            startActivity(intent)
        }

        binding.btnContractDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.apply {
                setTitle("Xác nhận")
                setMessage("Bạn có chắc chắn muốn xoá hợp đồng này?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                    performDeleteContract()
                }
            }
            dialog.create()
            dialog.show()
        }

        // Thanh lý
        binding.btnContractTermination.setOnClickListener {
            val bill = BillModel(
                contract.contractID,
                contract.roomID,
                DateFormatUtils.convertToMySQLDate(DateFormatUtils.getCurrentDateAsString()).toString(),
                DateFormatUtils.convertToMySQLDate(contract.startDate).toString(),
                DateFormatUtils.convertToMySQLDate(DateFormatUtils.dateAfterOneMonth(contract.startDate, contract.duration)).toString(),
                contract.price,
                0,
                "",
                0
            )
            val dialog = AlertDialog.Builder(requireContext())
            dialog.apply {
                setTitle("Xác nhận")
                setMessage("Bạn có chắc chắn muốn thanh hợp đồng này?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                    BillDAO(requireContext()).performAddBill(bill)
                    performDeleteContract()
                }
            }
            dialog.create()
            dialog.show()
        }
        binding.btnRoomDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.apply {
                setTitle("Xác nhận")
                setMessage("Dữ liệu phòng ảnh đến thông tin hoá đơn và thống kê, khi xoá sẽ không khôi phục lại được. Bạn chắc chắn xoá?")
                setCancelable(false)
                setNegativeButton("Huỷ") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                setPositiveButton("Đồng ý") { dialogInterface: DialogInterface, i: Int ->
                    performDeleteRoom()
                }
            }
            dialog.create()
            dialog.show()
        }
    }

    private fun performDeleteContract() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            if (roomID > 0) {
                RetrofitClient.apiService.deleteContract(roomID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ResponseBody> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(responseBody: ResponseBody) {
                            val response = responseBody.string()
                            toast(response)
                            if (response == "Xoá hợp đồng thành công") {
                                binding.cartViewContract.visibility = View.GONE
                            }
                        }

                        override fun onError(e: Throwable) {
                            toast("Không thể thực hiện")
                        }

                        override fun onComplete() {

                        }
                    })
            }
        } else {
            NetworkUtils.showToast(requireContext())
        }
    }/* END performDeleteRoom() */

    private fun performGetContract() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            if (roomID > 0) {
                RetrofitClient.apiService.getContractDetailed(0, roomID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ContractDetailedModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNext(obj: ContractDetailedModel) {
                            contract = obj
                            with(binding) {
                                contractID = obj.contractID

                                tvContractID.text = "Hợp đồng #${obj.contractID}"

                                val startDay = obj.startDate
                                val endDay =
                                    if (obj.endDate == "00-00-0000") "[Chưa xác định thời gian]" else obj.endDate
                                tvDate.text = "Từ $startDay đến $endDay"

                                binding.cartViewContract.visibility = View.VISIBLE

                                // Lưu trứ contract
                                //ContractManager.setContract(contract)
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
    }/* END performGetContract() */

    private fun performDeleteRoom() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            if (roomID > 0) {
                RetrofitClient.apiService.deleteRoom(roomID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ResponseBody> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(responseBody: ResponseBody) {
                            val response = responseBody.string()
                            toast(response)
                            if (response == "Xoá phòng thành công") {
                                requireActivity().finish()
                            }
                        }

                        override fun onError(e: Throwable) {
                            toast("Không thể thực hiện")
                        }

                        override fun onComplete() {

                        }
                    })
            }
        } else {
            NetworkUtils.showToast(requireContext())
        }
    }/* END performDeleteRoom() */

    private fun performGetRoomDetailed() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            if (roomID > 0) {
                RetrofitClient.apiService.getRoomDetailed(roomID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<RoomModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNext(room: RoomModel) {
                            with(binding) {
                                tvStatus.text =
                                    if (room.status == 1) "Đang cho thuê" else "Phòng trống"
                                tvPrice.text =
                                    "${DecimalFormat("###,###,###").format(room.price)} đ"
                                tvAcreage.text =
                                    "${room.acreage} ${resources.getString(R.string.square_meter_symbol)}"
                                tvFloors.text = room.floors.toString()
                                tvNumberBedroom.text = room.numberBedroom.toString()
                                tvNumberLivingroom.text = room.numberLivingroom.toString()
                                tvNumberPeopleLimit.text = room.numberPeopleLimit.toString()
                                tvDeposits.text =
                                    "${DecimalFormat("###,###,###").format(room.deposits)} đ"
                                edtDescribe.setText(room.describe)
                                edtNote.setText(room.note)
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
    }/* END performGetRoomDetailed() */

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        performGetRoomDetailed()
        performGetContract()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        disposable?.dispose()
    }
}