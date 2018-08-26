package br.com.gdgbrasilia.meetup.app.view.components

import android.app.Activity
import android.support.v7.widget.AppCompatTextView
import android.view.View
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.vo.ReverseGeocodeResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(val context: Activity) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        if (marker.tag is ViewType) return null
        val view = context.layoutInflater.inflate(R.layout.layout_info_window_map_view, null)
        val address = view.findViewById<AppCompatTextView>(R.id.infoWindowAddress)
        val loading = view.findViewById<AppCompatTextView>(R.id.infoWindowLoading)

        val tag = marker.tag
        if (tag != null) {
            if (tag is ReverseGeocodeResult) {
                val displayName = tag.displayName.split(",")
                address.text = "${displayName[0]}, ${displayName[1]}"
            } else {
                return null
            }
        }



        return view
    }

}