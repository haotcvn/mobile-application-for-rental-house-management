package com.tranconghaoit.homemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.Locale

class ContextUtils (baseContext: Context) : ContextWrapper(baseContext) {
    companion object {
        var language = "en"

        @Suppress("DEPRECATION")
        @SuppressLint("ObsoleteSdkInt")
        fun updateLocale(context: Context, localeToSwitch: Locale) : ContextWrapper {
            var newContext = context
            val resources = context.resources
            val config = resources.configuration
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitch)
                LocaleList.setDefault(localeList)
                config.setLocales(localeList)
            } else {
                config.locale = localeToSwitch
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                newContext = context.createConfigurationContext(config)
            } else {
                resources.updateConfiguration(config, resources.displayMetrics)
            }
            return ContextUtils(newContext)
        }
    }
}