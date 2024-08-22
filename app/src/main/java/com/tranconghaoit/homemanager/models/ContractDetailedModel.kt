package com.tranconghaoit.homemanager.models

import java.io.Serializable

class ContractDetailedModel(
    val contractID: Int,
    val roomID: Int,
    val renterID: Int,
    val startDate: String,
    val endDate: String,
    val duration: Int,
    val price: Int,
    val deposit: Int,
    val creator: String,
    val renterName: String,
    val roomName: String,
    val numberPhone: String
): Serializable