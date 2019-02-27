package com.zuhair.apppreferencemanager

import android.app.Application
import com.zuhair.apppermissionmanager.AppPermissionManager

class APMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPermissionManager.init(this)
    }
}