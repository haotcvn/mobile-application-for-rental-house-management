package com.tranconghaoit.homemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.databinding.FragmentFourBinding
import com.tranconghaoit.homemanager.databinding.FragmentNotificationSystemBinding
import io.reactivex.rxjava3.disposables.Disposable

class NotificationSystemFragment : Fragment() {
    private var _binding: FragmentNotificationSystemBinding? = null
    private val binding: FragmentNotificationSystemBinding get() = _binding!!
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentNotificationSystemBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }
}