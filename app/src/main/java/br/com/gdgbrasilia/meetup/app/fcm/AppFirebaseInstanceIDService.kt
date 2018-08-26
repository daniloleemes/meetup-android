package br.com.gdgbrasilia.meetup.app.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService

class AppFirebaseInstanceIDService : FirebaseInstanceIdService() {

    private val TAG = "FirebaseInstance"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "FirebaseInstanceIdService started")
    }

}