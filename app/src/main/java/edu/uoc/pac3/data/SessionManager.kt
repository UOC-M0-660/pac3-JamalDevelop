package edu.uoc.pac3.data

import android.content.Context
import android.util.Log
import edu.uoc.pac3.R
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.user.User

/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(context: Context) {

    private val TAG = "SessionManager"
    private val sharedPreferences = context.getSharedPreferences(R.string.preference_file_key.toString(), Context.MODE_PRIVATE)

    fun isUserAvailable(): Boolean {
        return (getAccessToken()?.isNotEmpty() ?:false) && (getRefreshToken()?.isNotEmpty() ?: false)
    }

    fun getAccessToken(): String? {
        // TODO: Implement
        return sharedPreferences.getString(OAuthConstants.ACCESS_TOKEN_PREFERENCES, null)
    }

    fun saveAccessToken(accessToken: String) {
        // TODO("Save Access Token")
        sharedPreferences.edit().putString(OAuthConstants.ACCESS_TOKEN_PREFERENCES, accessToken).apply()
        Log.d(TAG, "accessToken saved!")
    }

    fun clearAccessToken() {
        // TODO("Clear Access Token")
        sharedPreferences.edit().putString(OAuthConstants.ACCESS_TOKEN_PREFERENCES, "").clear().apply()
//        sharedPreferences.edit().remove(OAuthConstants.ACCESS_TOKEN_PREFERENCES).apply()
        Log.d(TAG, "accessToken cleared!")
    }

    fun getRefreshToken(): String? {
        // TODO("Get Refresh Token")
        return sharedPreferences.getString(OAuthConstants.REFRESH_TOKEN_PREFERENCES, null)
    }

    fun saveRefreshToken(refreshToken: String) {
        // TODO("Save Refresh Token")
        sharedPreferences.edit().putString(OAuthConstants.REFRESH_TOKEN_PREFERENCES, refreshToken).apply()
        Log.d(TAG, "refreshToken saved!")
    }

    fun clearRefreshToken() {
        // TODO("Clear Refresh Token")
        sharedPreferences.edit().putString(OAuthConstants.REFRESH_TOKEN_PREFERENCES, "").clear().apply()
//        sharedPreferences.edit().remove(OAuthConstants.REFRESH_TOKEN_PREFERENCES).apply()
        Log.d(TAG, "refreshToken cleared!")
    }

}