package br.com.gdgbrasilia.meetup.app.data

import android.location.Location
import br.com.gdgbrasilia.meetup.app.business.vo.Usuario
import com.google.android.gms.maps.CameraUpdate

object AppStatics {

    var currentUser: Usuario? = null
    var userCurrentLocation: Location? = null
    var currentCameraPosition: CameraUpdate? = null
    var refreshMap = false

}