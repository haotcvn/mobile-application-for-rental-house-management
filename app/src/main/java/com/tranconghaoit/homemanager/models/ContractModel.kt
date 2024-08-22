package com.tranconghaoit.homemanager.models

class ContractModel(
    val contractID: Int?,
    val renterID: Int,
    val roomID: Int,
    val startDate: String,
    val endDate: String,
    val duration: Int,
    val price: Int,
    val deposit: Int,
    val status: Int
)