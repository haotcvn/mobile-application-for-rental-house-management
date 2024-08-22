package com.tranconghaoit.homemanager.utils

object Utils {
    fun checkEmail(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }
        if (email.length < 6) {
            return false
        }
        val at = email.indexOf('@')
        val period = email.indexOf('.')

        if (at < 0) {
            return false
        }
        if (period < 0) {
            return false
        }
        if (at > period) {
            return false
        }
        if (period - at < 2) {
            return false
        }
        if (at == 0) {
            return false
        }
        if (period == email.length - 1) {
            return false
        }
        return true
    }
}