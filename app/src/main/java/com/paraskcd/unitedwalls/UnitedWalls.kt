package com.paraskcd.unitedwalls

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UnitedWalls: Application() {
    var preferences: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences(getProcessName() + "_preferences", MODE_PRIVATE)
        Log.d("PREFs", preferences?.getBoolean("privacy-policy-accepted", false).toString())
    }
}