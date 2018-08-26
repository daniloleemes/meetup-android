package br.com.gdgbrasilia.meetup.app.business.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReverseGeocodeResult(val place_id: String,
                                val lat: String,
                                val lon: String,
                                @SerializedName("display_name") val displayName: String) : Serializable