package com.zuhair.apppermissionmanager

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS
import android.support.annotation.IntDef
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_BODY_SENSOR_PERMISSION
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_CALENDAR_PERMISSION
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_CAMERA_PERMISSION
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_CONTACTS_PERMISSION
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_LOCATION_PERMISSION
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_MICROPHONE_PERMISSION
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_PHONE_PERMISSION
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_SMS_PERMISSION
import com.zuhair.apppermissionmanager.AppPermissionManager.Permission.Companion.REQUEST_STORAGE_PERMISSION
import com.zuhair.apppermissions.permissions.PermissionPrefs
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*


class AppPermissionManager private constructor(context: Context) {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(REQUEST_CAMERA_PERMISSION, REQUEST_LOCATION_PERMISSION, REQUEST_MICROPHONE_PERMISSION, REQUEST_CALENDAR_PERMISSION,
            REQUEST_CONTACTS_PERMISSION, REQUEST_STORAGE_PERMISSION,
            REQUEST_PHONE_PERMISSION, REQUEST_BODY_SENSOR_PERMISSION,
            REQUEST_SMS_PERMISSION)

    annotation class Permission {
        companion object {
            const val REQUEST_CAMERA_PERMISSION = 0
            const val REQUEST_LOCATION_PERMISSION = 1
            const val REQUEST_MICROPHONE_PERMISSION = 2
            const val REQUEST_CALENDAR_PERMISSION = 3
            const val REQUEST_CONTACTS_PERMISSION = 4
            const val REQUEST_STORAGE_PERMISSION = 5
            const val REQUEST_PHONE_PERMISSION = 6
            const val REQUEST_BODY_SENSOR_PERMISSION = 7
            const val REQUEST_SMS_PERMISSION = 8
        }
    }


    private var mContext: Context = context
    var subject : PublishSubject<PermResult> = PublishSubject.create()

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var mInstance: AppPermissionManager
        @JvmStatic
        fun init(context: Context) {
            PermissionPrefs.init(context)
            mInstance = AppPermissionManager(context)
        }

        @JvmStatic
        fun get(): AppPermissionManager {
            return mInstance
        }
    }

    fun isCameraGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext, CAMERA) == PERMISSION_GRANTED
    }

    private fun getCameraPermissions(): List<String> {
        return listOf(CAMERA)
    }

    fun requestCameraPermission(): PublishSubject<PermResult> {
        return requestPermissions(REQUEST_CAMERA_PERMISSION)
    }

    fun hasAskedForCameraPermission(): Boolean {
        return PermissionPrefs.get()
                .isCameraPermissionsAsked()
    }

    fun shouldShowCameraRationale(fragment: Fragment): Boolean {
        return !isCameraGranted() && shouldShowRequestPermissionRationale(fragment, CAMERA)
    }

    private fun shouldShowCameraRationale(activity: Activity): Boolean {
        return !isCameraGranted() && shouldShowRequestPermissionRationale(activity, CAMERA)
    }

    fun neverAskForCamera(fragment: Fragment): Boolean {
        return hasAskedForCameraPermission() != shouldShowCameraRationale(fragment)
    }

    fun neverAskForCamera(activity: Activity): Boolean {
        return hasAskedForCameraPermission() != shouldShowCameraRationale(activity)
    }

    fun isLocationGranted(): Boolean {
        return isFineLocationGranted() || isCoarseLocationGranted()
    }

    private fun isFineLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
    }

    private fun isCoarseLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
    }

    private fun getLocationPermissions(): List<String> {
        val perms = ArrayList<String>()
        perms.add(ACCESS_FINE_LOCATION)
        perms.add(ACCESS_COARSE_LOCATION)
        return perms
    }

    fun requestLocationPermission(): PublishSubject<PermResult> {
        return requestPermissions(REQUEST_LOCATION_PERMISSION)
    }

    private fun hasAskedForLocationPermission(): Boolean {
        return PermissionPrefs.get()
                .isLocationPermissionsAsked()
    }

    fun shouldShowLocationRationale(fragment: Fragment): Boolean {
        return (!isLocationGranted()
                && shouldShowRequestPermissionRationale(fragment, ACCESS_FINE_LOCATION)
                && shouldShowRequestPermissionRationale(fragment, ACCESS_COARSE_LOCATION))
    }

    private fun shouldShowLocationRationale(activity: Activity): Boolean {
        return (!isLocationGranted()
                && shouldShowRequestPermissionRationale(activity, ACCESS_FINE_LOCATION)
                && shouldShowRequestPermissionRationale(activity, ACCESS_COARSE_LOCATION))
    }

    fun neverAskForLocation(fragment: Fragment): Boolean {
        return hasAskedForLocationPermission() != shouldShowLocationRationale(
                fragment)
    }

    fun neverAskForLocation(activity: Activity): Boolean {
        return hasAskedForLocationPermission() != shouldShowLocationRationale(
                activity)
    }

    fun isMicrophoneGranted(): Boolean {
       return ContextCompat.checkSelfPermission(mContext,
                RECORD_AUDIO) == PERMISSION_GRANTED
    }

    private fun getMicrophonePermissions(): List<String> {
        return listOf(RECORD_AUDIO)
    }

    fun requestMicrophonePermission(): PublishSubject<PermResult> {
        return requestPermissions(REQUEST_MICROPHONE_PERMISSION)
    }

    fun hasAskedForMicrophonePermission(): Boolean {
        return PermissionPrefs.get()
                .isAudioPermissionsAsked()
    }

    fun shouldShowMicrophoneRationale(fragment: Fragment): Boolean {
        return !isMicrophoneGranted() && shouldShowRequestPermissionRationale(fragment, RECORD_AUDIO)
    }

    private fun shouldShowMicrophoneRationale(activity: Activity): Boolean {
        return !isMicrophoneGranted() && shouldShowRequestPermissionRationale(activity, RECORD_AUDIO)
    }

    fun neverAskForMicrophone(fragment: Fragment): Boolean {
        return hasAskedForMicrophonePermission() != shouldShowMicrophoneRationale(fragment)
    }

    fun neverAskForMicrophone(activity: Activity): Boolean {
        return hasAskedForMicrophonePermission() != shouldShowMicrophoneRationale(activity)
    }

    fun isCalendarGranted(): Boolean {
        return isReadCalendarGranted() || isWriteCalendarGranted()
    }

    private fun isReadCalendarGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext, READ_CALENDAR) == PERMISSION_GRANTED
    }

    private fun isWriteCalendarGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext, WRITE_CALENDAR) == PERMISSION_GRANTED
    }

    private fun getCalendarPermissions(): List<String> {
        val perms = ArrayList<String>()
        perms.add(READ_CALENDAR)
        perms.add(WRITE_CALENDAR)
        return perms
    }

    fun requestCalendarPermission(): Observable<PermResult> {
        return requestPermissions(REQUEST_CALENDAR_PERMISSION)
    }

    fun hasAskedForCalendarPermission(): Boolean {
        return PermissionPrefs.get()
                .isCalendarPermissionsAsked()
    }


    fun shouldShowCalendarRationale(fragment: Fragment): Boolean {
        return (!isCalendarGranted()
                && shouldShowRequestPermissionRationale(fragment,
                READ_CALENDAR)
                && shouldShowRequestPermissionRationale(fragment,
                WRITE_CALENDAR))
    }

    private fun shouldShowCalendarRationale(activity: Activity): Boolean {
        return (!isCalendarGranted()
                && shouldShowRequestPermissionRationale(activity,
                READ_CALENDAR)
                && shouldShowRequestPermissionRationale(activity,
                WRITE_CALENDAR))
    }

    fun neverAskForCalendar(fragment: Fragment): Boolean {
        return hasAskedForCalendarPermission() != shouldShowCalendarRationale(fragment)
    }

    fun neverAskForCalendar(activity: Activity): Boolean {
        return hasAskedForCalendarPermission() != shouldShowCalendarRationale(activity)
    }

    fun isContactsGranted(): Boolean {
        return (isReadContactsPermissionGranted()
                || isWriteContactsPermissionGranted()
                || isGetAccountsPermissionGranted())
    }

    private fun isReadContactsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext, READ_CONTACTS) == PERMISSION_GRANTED
    }

    private fun isWriteContactsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext, WRITE_CONTACTS) == PERMISSION_GRANTED
    }

    private fun isGetAccountsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext, GET_ACCOUNTS) == PERMISSION_GRANTED
    }

    private fun getContactsPermissions(): List<String> {
        val perms = ArrayList<String>()
        perms.add(READ_CONTACTS)
        perms.add(WRITE_CONTACTS)
        perms.add(GET_ACCOUNTS)
        return perms
    }

    fun requestContactsPermission(): PublishSubject<PermResult> {
        return requestPermissions(REQUEST_CONTACTS_PERMISSION)
    }

    fun hasAskedForContactsPermission(): Boolean {
        return PermissionPrefs.get()
                .isContactsPermissionsAsked()
    }

     fun shouldShowContactsRationale(fragment: Fragment): Boolean {
        return (!isContactsGranted()
                && shouldShowRequestPermissionRationale(fragment,
                READ_CONTACTS)
                && shouldShowRequestPermissionRationale(fragment,
                WRITE_CONTACTS)
                && shouldShowRequestPermissionRationale(fragment,
                GET_ACCOUNTS))
    }

    private fun shouldShowContactsRationale(activity: Activity): Boolean {
        return (!isContactsGranted()
                && shouldShowRequestPermissionRationale(activity,
                READ_CONTACTS)
                && shouldShowRequestPermissionRationale(activity,
                WRITE_CONTACTS)
                && shouldShowRequestPermissionRationale(activity,
                GET_ACCOUNTS))
    }

    fun neverAskForContacts(fragment: Fragment): Boolean {
        return hasAskedForContactsPermission() != shouldShowContactsRationale(fragment)
    }

    fun neverAskForContacts(activity: Activity): Boolean {
        return hasAskedForContactsPermission() != shouldShowContactsRationale(activity)
    }

    fun isPhoneGranted(): Boolean {
        return isOnePhoneGranted()
    }

    private fun isOnePhoneGranted(): Boolean {
        return (isReadPhoneStateGranted()
                || isCallPhoneGranted()
                || isReadCallLogGranted()
                || isWriteCallLogGranted()
                || isAddVoicemailGranted()
                || isUseSipGranted()
                || isProcessOutgoingCallsGranted())
    }

    private fun isReadPhoneStateGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                READ_PHONE_STATE) == PERMISSION_GRANTED
    }

    private fun isCallPhoneGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                CALL_PHONE) == PERMISSION_GRANTED
    }

    private fun isReadCallLogGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                READ_CALL_LOG) == PERMISSION_GRANTED
    }

    private fun isWriteCallLogGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                WRITE_CALL_LOG) == PERMISSION_GRANTED
    }

    private fun isAddVoicemailGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                ADD_VOICEMAIL) == PERMISSION_GRANTED
    }

    private fun isUseSipGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                USE_SIP) == PERMISSION_GRANTED
    }

    private fun isProcessOutgoingCallsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                PROCESS_OUTGOING_CALLS) == PERMISSION_GRANTED
    }

    private fun getPhonePermissions(): List<String> {
        val perms = ArrayList<String>()
        perms.add(READ_PHONE_STATE)
        perms.add(CALL_PHONE)
        perms.add(READ_CALL_LOG)
        perms.add(WRITE_CALL_LOG)
        perms.add(ADD_VOICEMAIL)
        perms.add(USE_SIP)
        perms.add(PROCESS_OUTGOING_CALLS)
        return perms
    }

    fun requestPhonePermission(): PublishSubject<PermResult> {
        return requestPermissions(REQUEST_PHONE_PERMISSION)
    }

    fun hasAskedForPhonePermission(): Boolean {
        return PermissionPrefs.get()
                .isPhonePermissionsAsked()
    }

    private fun shouldShowRequestPhoneRationale(activity: Activity): Boolean {
        return (!isOnePhoneGranted()
                && shouldShowRequestPermissionRationale(activity, READ_PHONE_STATE)
                && shouldShowRequestPermissionRationale(activity, CALL_PHONE)
                && shouldShowRequestPermissionRationale(activity, READ_CALL_LOG)
                && shouldShowRequestPermissionRationale(activity, WRITE_CALL_LOG)
                && shouldShowRequestPermissionRationale(activity, ADD_VOICEMAIL)
                && shouldShowRequestPermissionRationale(activity, USE_SIP)
                && shouldShowRequestPermissionRationale(activity, PROCESS_OUTGOING_CALLS))
    }

    fun neverAskForPhone(activity: Activity): Boolean {
        return hasAskedForPhonePermission() != shouldShowRequestPhoneRationale(
                activity)
    }

    fun isStorageGranted(): Boolean {
        return isOneStorageGranted()
    }

    private fun isOneStorageGranted(): Boolean {
        return isWriteStorageGranted() || isReadStorageGranted()
    }

    private fun isReadStorageGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED
    }

    private fun isWriteStorageGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
    }

    private fun getStoragePermissions(): List<String> {
        val perms = ArrayList<String>()
        perms.add(WRITE_EXTERNAL_STORAGE)
        perms.add(READ_EXTERNAL_STORAGE)
        return perms
    }


    fun requestStoragePermission(): PublishSubject<PermResult> {
        return requestPermissions(REQUEST_STORAGE_PERMISSION)
    }

    fun hasAskedForStoragePermission(): Boolean {
        return PermissionPrefs.get()
                .isStoragePermissionsAsked()
    }

    fun shouldShowRequestStorageRationale(fragment: Fragment): Boolean {
        return (!isOneStorageGranted()
                && shouldShowRequestPermissionRationale(fragment, WRITE_EXTERNAL_STORAGE)
                && shouldShowRequestPermissionRationale(fragment, READ_EXTERNAL_STORAGE))
    }

    private fun shouldShowRequestStorageRationale(activity: Activity): Boolean {
        return (!isOneStorageGranted()
                && shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE)
                && shouldShowRequestPermissionRationale(activity, READ_EXTERNAL_STORAGE))
    }

    fun neverAskForStorage(fragment: Fragment): Boolean {
        return hasAskedForStoragePermission() != shouldShowRequestStorageRationale(
                fragment)
    }

    fun neverAskForStorage(activity: Activity): Boolean {
        return hasAskedForStoragePermission() != shouldShowRequestStorageRationale(
                activity)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    fun isBodySensorGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext, BODY_SENSORS) == PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private fun getBodySensorPermissions(): List<String> {
        return listOf(BODY_SENSORS)
    }

    public fun requestBodySensorPermission(): PublishSubject<PermResult> {
        return requestPermissions(REQUEST_BODY_SENSOR_PERMISSION)
    }

    fun hasAskedForBodySensorPermission(): Boolean {
        return PermissionPrefs.get()
                .isBodySensorsPermissionsAsked()
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    fun shouldShowBodySensorRationale(fragment: Fragment): Boolean {
        return !isBodySensorGranted() && shouldShowRequestPermissionRationale(fragment,
                BODY_SENSORS)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private fun shouldShowBodySensorRationale(activity: Activity): Boolean {
        return !isBodySensorGranted() && shouldShowRequestPermissionRationale(activity,
                BODY_SENSORS)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    fun neverAskForBodySensor(fragment: Fragment): Boolean {
        return hasAskedForBodySensorPermission() != shouldShowBodySensorRationale(fragment)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    fun neverAskForBodySensor(activity: Activity): Boolean {
        return hasAskedForBodySensorPermission() != shouldShowBodySensorRationale(activity)
    }

    fun isSmsGranted(): Boolean {
        return isOneSmsGranted()
    }

    private fun isOneSmsGranted(): Boolean {
        return (isSendSmsGranted()
                || isReceiveSmsGranted()
                || isReadSmsGranted()
                || isReceiveWapPushGranted()
                || isReceiveMmsGranted())
    }

    private fun isSendSmsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                SEND_SMS) == PERMISSION_GRANTED
    }

    private fun isReceiveSmsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                RECEIVE_SMS) == PERMISSION_GRANTED
    }

    private fun isReadSmsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                READ_SMS) == PERMISSION_GRANTED
    }

    private fun isReceiveWapPushGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                RECEIVE_WAP_PUSH) == PERMISSION_GRANTED
    }

    private fun isReceiveMmsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(mContext,
                RECEIVE_MMS) == PERMISSION_GRANTED
    }

    private fun getSmsPermissions(): List<String> {
        val perms = ArrayList<String>()
        perms.add(SEND_SMS)
        perms.add(RECEIVE_SMS)
        perms.add(READ_SMS)
        perms.add(RECEIVE_WAP_PUSH)
        perms.add(RECEIVE_MMS)
        return perms
    }


    fun requestSmsPermission(): PublishSubject<PermResult> {
        return requestPermissions(REQUEST_SMS_PERMISSION)
    }

    /**
     * @return if storage permission has been previously requested.
     */
    fun hasAskedForSmsPermission(): Boolean {
        return PermissionPrefs.get()
                .isSmsPermissionsAsked()
    }

    fun shouldShowRequestSmsRationale(fragment: Fragment): Boolean {
        return (!isOneSmsGranted()
                && shouldShowRequestPermissionRationale(fragment, SEND_SMS)
                && shouldShowRequestPermissionRationale(fragment, RECEIVE_SMS)
                && shouldShowRequestPermissionRationale(fragment, READ_SMS)
                && shouldShowRequestPermissionRationale(fragment, RECEIVE_WAP_PUSH)
                && shouldShowRequestPermissionRationale(fragment, RECEIVE_MMS))
    }

    private fun shouldShowRequestSmsRationale(activity: Activity): Boolean {
        return (!isOneSmsGranted()
                && shouldShowRequestPermissionRationale(activity, SEND_SMS)
                && shouldShowRequestPermissionRationale(activity, RECEIVE_SMS)
                && shouldShowRequestPermissionRationale(activity, READ_SMS)
                && shouldShowRequestPermissionRationale(activity, RECEIVE_WAP_PUSH)
                && shouldShowRequestPermissionRationale(activity, RECEIVE_MMS))
    }

    /**
     * See [.neverAskForCamera]
     *
     * @param fragment to check with
     * @return if should not ask
     */
    fun neverAskForSms(fragment: Fragment): Boolean {
        return hasAskedForSmsPermission() != shouldShowRequestSmsRationale(
                fragment)
    }

    /**
     * See [.neverAskForCamera]
     *
     * @param activity to check with
     * @return if should not ask
     */
    fun neverAskForSms(activity: Activity): Boolean {
        return hasAskedForSmsPermission() != shouldShowRequestSmsRationale(
                activity)
    }

    /**
     * Wraps [Fragment.shouldShowRequestPermissionRationale]
     *
     * @param fragment   checking for permissions
     * @param permission to check
     * @return if we should show
     */
    private fun shouldShowRequestPermissionRationale(fragment: Fragment,
                                                     permission: String): Boolean {
        return fragment.shouldShowRequestPermissionRationale(permission)
    }

    private fun shouldShowRequestPermissionRationale(activity: Activity,
                                                     permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    fun intentToAppSettings(activity: Activity) {
        //Open the specific App Info page:
        var intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + mContext.packageName)
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        } else {
            intent = Intent(ACTION_MANAGE_APPLICATIONS_SETTINGS)
            if (intent.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(intent)
            }
        }
    }

    private fun markPermissionsAsked(@Permission vararg permissions: Int) {
        for (permission in permissions) {
            markPermissionAsked(permission)
        }
    }

    fun requestPermissions(vararg permissions: Int): PublishSubject<PermResult> {

        return if (checkPermissionsGranted(*permissions)) {
            subject.onNext(PermResult(true))
            subject
        } else {
            markPermissionsAsked(*permissions)
           return requestPermission(*getPermissionsToRequest(*permissions))
        }
    }

    private fun checkPermissionsGranted(@Permission vararg permissions: Int): Boolean {
        for (permission in permissions) {
            if (!isPermissionGranted(permission)) {
                return false
            }
        }
        return true
    }


    @SuppressLint("SwitchIntDef")
    private fun isPermissionGranted(@Permission permission: Int): Boolean {

        when (permission) {
            REQUEST_CAMERA_PERMISSION -> return isCameraGranted()
            REQUEST_LOCATION_PERMISSION -> return isLocationGranted()
            REQUEST_MICROPHONE_PERMISSION -> return isMicrophoneGranted()
            REQUEST_CALENDAR_PERMISSION -> return isCalendarGranted()
            REQUEST_CONTACTS_PERMISSION -> return isContactsGranted()
            REQUEST_STORAGE_PERMISSION -> return isStorageGranted()
            REQUEST_PHONE_PERMISSION -> return isPhoneGranted()
            REQUEST_BODY_SENSOR_PERMISSION -> return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                isBodySensorGranted()
            } else {
                return false
            }
            REQUEST_SMS_PERMISSION -> return isSmsGranted()
        }
        return false
    }

    @SuppressLint("SwitchIntDef")
    private fun markPermissionAsked(@Permission permission: Int) {
        when (permission) {
            REQUEST_CAMERA_PERMISSION -> {
                PermissionPrefs.get()
                        .setCameraPermissionsAsked()
                return
            }
            REQUEST_LOCATION_PERMISSION -> {
                PermissionPrefs.get()
                        .setLocationPermissionsAsked()
                return
            }
            REQUEST_MICROPHONE_PERMISSION -> {
                PermissionPrefs.get()
                        .setMicrophonePermissionsAsked()
                return
            }
            REQUEST_CALENDAR_PERMISSION -> {
                PermissionPrefs.get()
                        .setCalendarPermissionsAsked()
                return
            }
            REQUEST_CONTACTS_PERMISSION -> {
                PermissionPrefs.get()
                        .setContactsPermissionsAsked()
                return
            }
            REQUEST_STORAGE_PERMISSION -> {
                PermissionPrefs.get()
                        .setStoragePermissionsAsked()
                return
            }
            REQUEST_PHONE_PERMISSION -> {
                PermissionPrefs.get()
                        .setPhonePermissionsAsked()
                return
            }
            REQUEST_BODY_SENSOR_PERMISSION -> {
                PermissionPrefs.get()
                        .setBodySensorsPermissionsAsked()
                return
            }
            REQUEST_SMS_PERMISSION -> {
                PermissionPrefs.get()
                        .setSmsPermissionsAsked()
            }

        }
    }

    private fun getPermissionsToRequest(@Permission vararg permissions: Int): Array<String> {
        val toRequest = ArrayList<String>()
        for (permission in permissions) {
            toRequest.addAll(getPermissionsFor(permission))
        }
        return toRequest.toTypedArray()
    }

    @SuppressLint("SwitchIntDef")
    private fun getPermissionsFor(@Permission permission: Int): List<String> {
        return when (permission) {
            REQUEST_CAMERA_PERMISSION -> getCameraPermissions()
            REQUEST_LOCATION_PERMISSION -> getLocationPermissions()
            REQUEST_MICROPHONE_PERMISSION -> getMicrophonePermissions()
            REQUEST_CALENDAR_PERMISSION -> getCalendarPermissions()
            REQUEST_CONTACTS_PERMISSION -> getContactsPermissions()
            REQUEST_STORAGE_PERMISSION -> getStoragePermissions()
            REQUEST_PHONE_PERMISSION -> getPhonePermissions()
            REQUEST_BODY_SENSOR_PERMISSION -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                getBodySensorPermissions()
            } else {
                emptyList()
            }
            REQUEST_SMS_PERMISSION -> getSmsPermissions()
            else -> emptyList()
        }
    }



    /**
     * Request a permission.
     *
     * @param permissions to request
     */
    private fun requestPermission(vararg permissions: String): PublishSubject<PermResult> {

        if (isAskingForPermissions()) {
            throw IllegalStateException(
                    "Already requesting permissions, cannot ask for permissions.")
        }
        startPermissionsActivity(*permissions)
        return subject
    }


    private fun isAskingForPermissions(): Boolean {
        return PermissionRequestActivity.isAskingForPermissions()
    }

    private fun startPermissionsActivity(vararg permissions: String) {
        val i = PermissionRequestActivity.getIntent(mContext, *permissions)
        mContext.startActivity(i)
    }

    fun onRequestPermissionsResult(permissions: Array<String>,
                                                          grantResults: IntArray) {

        val granted = arePermissionsGranted(grantResults)
        subject.onNext(PermResult(granted))

    }

    private fun arePermissionsGranted(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun tearDown() {
        PermissionPrefs.get()
                .tearDown()
    }

}
