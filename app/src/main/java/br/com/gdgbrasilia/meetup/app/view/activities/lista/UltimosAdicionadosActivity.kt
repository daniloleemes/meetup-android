package br.com.gdgbrasilia.meetup.app.view.activities.lista

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.extensions.checkLocationPermission
import br.com.gdgbrasilia.meetup.app.view.adapters.ViewPagerAdapter
import br.com.gdgbrasilia.meetup.app.view.fragments.ultimos.UltimosEstabelecimentosFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.ultimos.UltimosEventosFragment
import kotlinx.android.synthetic.main.activity_ultimos_estabelecimentos.*
import kotlinx.android.synthetic.main.toolbar_main.*

class UltimosAdicionadosActivity : AppCompatActivity() {

    private val viewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimos_estabelecimentos)
        setupToolbar()
        setupViewPager()
    }

    private fun setupViewPager() {
        viewPagerAdapter.addFragment(UltimosEventosFragment(), "Eventos")
        viewPagerAdapter.addFragment(UltimosEstabelecimentosFragment(), "Estabelecimentos")
        ultimosViewPager.adapter = viewPagerAdapter
        ultimosTabLayout.setupWithViewPager(ultimosViewPager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Últimos adicionados"
    }

    fun getBestLocation(): Location? {
        val locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        var location: Location? = null

        if (AppStatics.userCurrentLocation != null) {
            location = AppStatics.userCurrentLocation!!
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (checkLocationPermission()) {
                val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                if (gpsLocation != null && networkLocation != null) {
                    location = if (gpsLocation.accuracy < networkLocation.accuracy) gpsLocation else networkLocation
                } else if (gpsLocation == null && networkLocation != null) {
                    location = networkLocation
                } else if (gpsLocation != null && networkLocation == null) {
                    location = gpsLocation
                }
            }
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }


        return location
    }
}
