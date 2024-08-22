package com.tranconghaoit.homemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.FragmentFiveBinding
import com.tranconghaoit.homemanager.models.UserModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FiveFragment : Fragment() {
    private var _binding: FragmentFiveBinding? = null
    private val binding: FragmentFiveBinding get() = _binding!!
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFiveBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        performGetUser()
    }
    private fun performGetUser() {
        if (NetworkUtils.haveNetworkConnection(requireContext())) {
            val userID = requireActivity().intent.getIntExtra("userID", -1)
            if (userID > 0) {
                RetrofitClient.apiService.getUser(userID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<UserModel> {
                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(user: UserModel) {
                            with(binding) {
                                tvUserName.text = user.name
                                tvNumberPhone.text = user.numberPhone
                                tvAddress.text = user.address
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}