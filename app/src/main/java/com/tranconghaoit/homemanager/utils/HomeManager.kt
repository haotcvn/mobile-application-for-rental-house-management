package com.tranconghaoit.homemanager.utils

import com.tranconghaoit.homemanager.models.BuildingModel

object HomeManager {
    private lateinit var home: BuildingModel
    private val homeList: MutableList<BuildingModel> = mutableListOf()

    fun setHome(home: BuildingModel) {
        this.home = home
    }

    fun getHome(): BuildingModel {
        return home
    }

    fun getHomeList(): List<BuildingModel> {
        return homeList
    }

    fun updateHomeList(newList: List<BuildingModel>) {
        homeList.clear()
        homeList.addAll(newList)
    }
}