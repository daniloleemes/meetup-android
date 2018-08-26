package br.com.gdgbrasilia.meetup.app.util.extensions

import android.location.Location
import com.google.android.gms.maps.GoogleMap

fun GoogleMap.calculateRadius(): Float {
    val visibleRegion = projection.visibleRegion

    val farRight = visibleRegion.farRight
    val farLeft = visibleRegion.farLeft
    val nearRight = visibleRegion.nearRight
    val nearLeft = visibleRegion.nearLeft

    val distanceWidth = FloatArray(2)
    Location.distanceBetween(
            (farRight.latitude + nearRight.latitude) / 2,
            (farRight.longitude + nearRight.longitude) / 2,
            (farLeft.latitude + nearLeft.latitude) / 2,
            (farLeft.longitude + nearLeft.longitude) / 2,
            distanceWidth
    )


    val distanceHeight = FloatArray(2)
    Location.distanceBetween(
            (farRight.latitude + nearRight.latitude) / 2,
            (farRight.longitude + nearRight.longitude) / 2,
            (farLeft.latitude + nearLeft.latitude) / 2,
            (farLeft.longitude + nearLeft.longitude) / 2,
            distanceHeight
    )

    val distance = if (distanceWidth[0] > distanceHeight[0]) {
        distanceWidth[0]
    } else {
        distanceHeight[0]
    }

    return distance
}