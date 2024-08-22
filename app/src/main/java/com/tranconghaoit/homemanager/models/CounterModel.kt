package com.tranconghaoit.homemanager.models

class CounterModel(
    val homeNumber: Int,
    val renterNumber: Int,
    val contractNumber: Int,
    val roomNumber: Int,
    val emptyRoomNumber: Int
) {
    override fun toString(): String {
        return "CounterModel(homeNumber=$homeNumber, renterNumber=$renterNumber, contractNumber=$contractNumber, roomNumber=$roomNumber, emptyRoomNumber=$emptyRoomNumber)"
    }
}