package com.tranconghaoit.homemanager.models

class BuildingModel(
    val homeID: Int,
    val userID: Int,
    val name: String,
    val userName: String,
    val roomNumber: Int,
    val renterNumber: Int,
    val numberFloors: Int,
    val price: Int,
    val describe: String,
    val address: String,
    val province: String,
    val district: String,
    val homeNote: String,
    val billNote: String,
    val status: Int
) {
    constructor () : this(0, 0, "", "", 0, 0, 0, 0, "", "", "", "", "", "",1)
    constructor(
        userID: Int,
        name: String,
        numberFloors: Int,
        price: Int,
        describe: String,
        address: String,
        province: String,
        district: String,
        homeNote: String,
        billNote: String,
        status: Int
    ) : this(
        0,
        userID,
        name,
        "",
        0,
        0,
        numberFloors,
        price,
        describe,
        address,
        province,
        district,
        homeNote,
        billNote,
        1
    )
    constructor(
        homeID: Int,
        name: String,
        numberFloors: Int,
        price: Int,
        describe: String,
        address: String,
        province: String,
        district: String,
        homeNote: String,
        billNote: String
    ) : this(
        homeID,
        0,
        name,
        "",
        0,
        0,
        numberFloors,
        price,
        describe,
        address,
        province,
        district,
        homeNote,
        billNote,
        1
    )
}