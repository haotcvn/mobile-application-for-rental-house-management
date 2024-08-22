package com.tranconghaoit.homemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tranconghaoit.homemanager.databinding.FragmentNotificationPromotionBinding
import io.reactivex.rxjava3.disposables.Disposable

class NotificationPromotionFragment : Fragment() {
    private var _binding: FragmentNotificationPromotionBinding? = null
    private val binding: FragmentNotificationPromotionBinding get() = _binding!!
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentNotificationPromotionBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}