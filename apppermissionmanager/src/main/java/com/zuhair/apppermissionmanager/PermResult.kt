package com.zuhair.apppermissionmanager

class PermResult constructor(private var isGranted: Boolean){

    fun isGranted(): Boolean {
        return isGranted
    }
}