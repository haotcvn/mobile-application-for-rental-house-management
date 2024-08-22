package com.tranconghaoit.homemanager.utils

import com.tranconghaoit.homemanager.models.ContractModel

object ContractManager {
    private lateinit var contract: ContractModel

    fun setContract(contract: ContractModel) {
        this.contract = contract
    }

    fun getContract(): ContractModel {
        return contract
    }
}