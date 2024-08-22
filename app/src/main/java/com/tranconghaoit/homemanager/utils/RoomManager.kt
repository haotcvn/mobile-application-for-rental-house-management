package com.tranconghaoit.homemanager.utils

import com.tranconghaoit.homemanager.models.RoomModel

object RoomManager {
    private lateinit var room: RoomModel
    fun setRoom(room: RoomModel) {
        this.room = room
    }

    fun getRoom(): RoomModel {
        return room
    }
}