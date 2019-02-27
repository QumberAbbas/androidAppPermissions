package com.zuhair.apppermissions.permissions

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PermissionPrefs private constructor(context: Context) {

    private val HAS_ASKED_FOR_CAMERA_KEY = "has_asked_for_camera"
    private val HAS_ASKED_FOR_LOCATION_KEY = "has_asked_for_location"
    private val HAS_ASKED_FOR_AUDIO_RECORDING_KEY = "has_asked_for_audio_recording"
    private val HAS_ASKED_FOR_CALENDAR_KEY = "has_asked_for_calendar"
    private val HAS_ASKED_FOR_CONTACTS_KEY = "has_asked_for_contacts"
    private val HAS_ASKED_FOR_CALLING_KEY = "has_asked_for_calling"
    private val HAS_ASKED_FOR_STORAGE_KEY = "has_asked_for_storage"
    private val HAS_ASKED_FOR_BODY_SENSORS_KEY = "has_asked_for_body_sensors"
    private val HAS_ASKED_FOR_SMS_KEY = "has_asked_for_sms"


    private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private var permissionPrefs: PermissionPrefs? = null
        @JvmStatic
        fun init(context: Context) {
            permissionPrefs = PermissionPrefs(context)
        }

        fun get(): PermissionPrefs {
            return permissionPrefs!!
        }

    }

    internal fun tearDown() {
        clearData()
        permissionPrefs = null
    }

    // ==== CAMERA =================================================================================

    internal fun setCameraPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_CAMERA_KEY, true)
                .apply()
    }

    internal fun isCameraPermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_CAMERA_KEY, false)
    }

    // ===== LOCATION ==============================================================================

    internal fun setLocationPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_LOCATION_KEY, true)
                .apply()
    }

    internal fun isLocationPermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_LOCATION_KEY, false)
    }

    // ===== MICROPHONE ============================================================================

    internal fun setMicrophonePermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_AUDIO_RECORDING_KEY, true)
                .apply()
    }

    internal fun isAudioPermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_AUDIO_RECORDING_KEY, false)
    }

    // ===== CALENDAR ==============================================================================

    internal fun setCalendarPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_CALENDAR_KEY, true)
                .apply()
    }

    internal fun isCalendarPermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_CALENDAR_KEY, false)
    }

    // ===== CONTACTS ==============================================================================

    internal fun setContactsPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_CONTACTS_KEY, true)
                .apply()
    }

    internal fun isContactsPermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_CONTACTS_KEY, false)
    }

    // ===== PHONE ==============================================================================

    internal fun setPhonePermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_CALLING_KEY, true)
                .apply()
    }

    internal fun isPhonePermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_CALLING_KEY, false)
    }

    // ===== STORAGE ===============================================================================

    internal fun setStoragePermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_STORAGE_KEY, true)
                .apply()
    }

    internal fun isStoragePermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_STORAGE_KEY, false)
    }

    // ===== BODY SENSORS ==============================================================================

    internal fun setBodySensorsPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_BODY_SENSORS_KEY, true)
                .apply()
    }

    internal fun isBodySensorsPermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_BODY_SENSORS_KEY, false)
    }

    // ===== SMS ==============================================================================

    internal fun setSmsPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_SMS_KEY, true)
                .apply()
    }

    internal fun isSmsPermissionsAsked(): Boolean {
        return preferences.getBoolean(HAS_ASKED_FOR_SMS_KEY, false)
    }

    // ===== TESTING ===============================================================================

    private fun clearData() {
        preferences.edit()
                .clear()
                .apply()
    }
}
