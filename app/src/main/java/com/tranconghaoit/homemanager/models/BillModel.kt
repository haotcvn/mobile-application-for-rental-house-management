package com.tranconghaoit.homemanager.models

import java.io.Serializable

class BillModel(
    val billID: Int,
    val contractID: Int,
    val roomID: Int,
    val issueDate: String,
    val fromDate: String,
    val toDate: String,
    val roomPrice: Int,
    val servicePrice: Int,
    val renterName: String,
    val renterPhone: String,
    val roomName: String,
    val total: Int,
    val note: String,
    val status: Int
) : Serializable {
    constructor(
        contractID: Int,
        roomID: Int,
        issueDate: String,
        fromDate: String,
        toDate: String,
        roomPrice: Int,
        servicePrice: Int,
        note: String,
        status: Int
    ) : this(
        0,
        contractID,
        roomID,
        issueDate,
        fromDate,
        toDate,
        roomPrice,
        servicePrice,
        "",
        "",
        "",
        0,
        "",
        status
    )

    override fun toString(): String {
        return "BillModel(billID=$billID, contractID=$contractID, roomID=$roomID, issueDate='$issueDate', fromDate='$fromDate', toDate='$toDate', roomPrice=$roomPrice, servicePrice=$servicePrice, roomName='$roomName', total=$total, status=$status)"
    }
}