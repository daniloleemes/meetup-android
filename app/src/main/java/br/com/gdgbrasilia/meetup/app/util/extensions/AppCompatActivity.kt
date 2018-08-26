package br.com.gdgbrasilia.meetup.app.util.extensions

import android.Manifest
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.data.AppConstants

fun <T : ViewModel> AppCompatActivity.getViewModel(type: Class<T>): T {
    return ViewModelProviders.of(this).get(type)
}

fun AppCompatActivity.requestLocationPermission() {
    ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
            AppConstants.LOCATION_PERMISSION_REQUEST_CODE)
}

fun AppCompatActivity.checkLocationPermission(): Boolean {
    return this.isPermissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
            this.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)

}