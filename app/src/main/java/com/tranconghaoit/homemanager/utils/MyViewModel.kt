package com.tranconghaoit.homemanager.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel:ViewModel() {
    val  reloadHome = MutableLiveData<Boolean>()
}