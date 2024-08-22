package com.tranconghaoit.homemanager.models

import java.io.Serializable

class RenterModel(
    val renterID: Int,
    val name: String,
    val numberPhone: String,
    val email: String,
    val birthday: String,
    val citizenIDNumber: String,
    val citizenIDIssueDate: String,
    val placeResidence: String,
    val address: String,
    val image: String?,
    val citizenImageFirstBefore: String?,
    val citizenImageFirstAfter: String?,
    val status: Int
) : Serializable {
    constructor(
        name: String,
        numberPhone: String,
        email: String,
        birthday: String,
        citizenIDNumber: String,
        citizenIDIssueDate: String,
        placeResidence: String,
        address: String,
        image: String?,
        citizenImageFirstBefore: String?,
        citizenImageFirstAfter: String?
    ) : this(0, name, numberPhone, email, birthday, citizenIDNumber, citizenIDIssueDate, placeResidence, address, image, citizenImageFirstBefore, citizenImageFirstAfter, 0)

    override fun toString(): String {
        return "RenterModel(renterID=$renterID, name='$name', numberPhone='$numberPhone', email='$email', birthday='$birthday', citizenIDNumber='$citizenIDNumber', citizenIDIssueDate='$citizenIDIssueDate', placeResidence='$placeResidence', address='$address', image=$image, citizenImageFirstBefore=$citizenImageFirstBefore, citizenImageFirstAfter=$citizenImageFirstAfter, status=$status)"
    }

}