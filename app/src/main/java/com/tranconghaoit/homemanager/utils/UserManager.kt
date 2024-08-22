package com.tranconghaoit.homemanager.utils

import com.tranconghaoit.homemanager.models.UserModel

object UserManager {
    private lateinit var user: UserModel

    fun setUser(user: UserModel) {
        this.user = user
    }

    fun getUser(): UserModel {
        return user
    }
}