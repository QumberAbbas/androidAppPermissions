package com.zuhair.apppreferencemanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.zuhair.apppermissionmanager.AppPermissionManager
import com.zuhair.apppermissionmanager.PermResult
import io.reactivex.disposables.Disposable

class FirstActivity : AppCompatActivity() {

    val disposables = ArrayList<Disposable?> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            when {
                AppPermissionManager.get().isLocationGranted() -> Toast.makeText(FirstActivity@this, "Already granted", Toast.LENGTH_SHORT).show()
                AppPermissionManager.get().neverAskForLocation(FirstActivity@this) -> Toast.makeText(FirstActivity@this, "User denied and said dont ask again", Toast.LENGTH_SHORT).show()
                else -> disposables.add(AppPermissionManager.get().requestLocationPermission().subscribe(::update, ::failure))
            }

        }

        var button1 = findViewById<Button>(R.id.button2)
        button1.setOnClickListener {

            disposables.add(AppPermissionManager.get().requestPermissions(AppPermissionManager.Permission.REQUEST_CAMERA_PERMISSION,
                    AppPermissionManager.Permission.REQUEST_STORAGE_PERMISSION).subscribe(::update, ::failure))

        }
    }

    fun update(permResult: PermResult){
        Toast.makeText(this, "Permission Granted"+permResult.isGranted(), Toast.LENGTH_SHORT).show()
    }

    fun failure(throwable: Throwable){
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        for( disposable in disposables){
            disposable?.dispose()
        }

        disposables.clear()
    }
}