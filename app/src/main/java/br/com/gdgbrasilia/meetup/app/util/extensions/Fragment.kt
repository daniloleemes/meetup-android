package br.com.gdgbrasilia.meetup.app.util.extensions

import android.Manifest
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import br.com.gdgbrasilia.meetup.app.data.AppConstants

fun <T : ViewModel> Fragment.getActivityViewModel(type: Class<T>): T {
    return ViewModelProviders.of(activity!!).get(type)
}

fun <T : ViewModel> Fragment.getViewModel(type: Class<T>): T {
    return ViewModelProviders.of(this).get(type)
}

fun Fragment.checkLocationPermission(): Boolean {
    return if (isAdded) {
        activity!!.isPermissionGranted(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                activity!!.isPermissionGranted(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
    } else {
        false
    }
}

fun Fragment.requestLocationPermission() {
    if (isAdded) {
        requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION),
                AppConstants.LOCATION_PERMISSION_REQUEST_CODE)
    }
}