package com.tranconghaoit.homemanager.models

class RoomModel(
    val roomID: Int,
    val homeID: Int?,
    val name: String,
    val price: Int,
    val floors: Int,
    val numberBedroom: Int,
    val numberLivingroom: Int,
    val acreage: Int,
    val numberPeopleLimit: Int,
    val deposits: Int, // Tiền đặt cọc
    val gender: String,
    val describe: String,
    val note: String,
    val status: Int
) {
    constructor(
        homeID: Int,
        name: String,
        price: Int,
        floors: Int,
        numberBedroom: Int,
        numberLivingroom: Int,
        acreage: Int,
        numberPeopleLimit: Int,
        deposits: Int, // Tiền đặt cọc
        gender: String,
        describe: String,
        note: String,
        status: Int
    ) : this(
        0,
        homeID,
        name,
        price,
        floors,
        numberBedroom,
        numberLivingroom,
        acreage,
        numberPeopleLimit,
        deposits,
        gender,
        describe,
        note,
        status
    )
}