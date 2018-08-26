package br.com.gdgbrasilia.meetup.app.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class AppFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseInstance"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "FirebaseMessagingService started")
    }
}