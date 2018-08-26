package br.com.gdgbrasilia.meetup.app.view.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.business.vo.ReverseGeocodeResult
import br.com.gdgbrasilia.meetup.app.data.AppConstants.LOCATION_PERMISSION_REQUEST_CODE
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.extensions.*
import br.com.gdgbrasilia.meetup.app.view.activities.cadastro.NovoCadastroActivity
import br.com.gdgbrasilia.meetup.app.view.activities.lista.FavoritosActivity
import br.com.gdgbrasilia.meetup.app.view.activities.lista.MinhasPublicacoesActivity
import br.com.gdgbrasilia.meetup.app.view.activities.lista.RankingActivity
import br.com.gdgbrasilia.meetup.app.view.activities.lista.UltimosAdicionadosActivity
import br.com.gdgbrasilia.meetup.app.view.bottomsheet.BottomsheetLocaListener
import br.com.gdgbrasilia.meetup.app.view.bottomsheet.BottomsheetLocal
import br.com.gdgbrasilia.meetup.app.view.components.CustomInfoWindowAdapter
import br.com.gdgbrasilia.meetup.app.view.fragments.filter.FilterMenuEstabelecimentoFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.filter.FilterMenuEventoFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.filter.FilterMenuFragment
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EventoViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.io.File
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity(), OnMapReadyCallback,
        FilterMenuFragment.FilterListener, BottomsheetLocaListener {

    private val estabelecimentoViewModel by lazy { getViewModel(EstabelecimentoViewModel::class.java) }
    private val eventoViewModel by lazy { getViewModel(EventoViewModel::class.java) }
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var googleMap: GoogleMap
    private var doubleBackToExitPressedOnce = false
    private var currentZoom = 15f
    private var mapMarkers: MutableList<Marker> = mutableListOf()
    private var isFilterEnabled = false

    /**
     * Default configuration
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupLocation()
        setupViews()
        setupMap(savedInstanceState)
        setupFiltro()
        novoEventoFab.setOnClickListener(novoEvento())
        centerLocationFab.setOnClickListener {
            googleMap.myLocation?.let {
                moveMapCamera(it, googleMap)
            }
        }
        resetTiltFab.setOnClickListener {
            val cameraPosition = CameraPosition.Builder().target(googleMap.cameraPosition.target)
                    .zoom(googleMap.cameraPosition.zoom)
                    .bearing(0f)
                    .tilt(0f)
                    .build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        changeTileFab.setOnClickListener {
            googleMap.mapType = if (googleMap.mapType == GoogleMap.MAP_TYPE_SATELLITE) GoogleMap.MAP_TYPE_NORMAL else GoogleMap.MAP_TYPE_SATELLITE
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

        if (AppStatics.refreshMap) {
            AppStatics.refreshMap = false

            mapMarkers.forEach { it.remove() }
            mapMarkers.clear()
            setupMarkers()
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        drawerLayout.closeDrawers()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    googleMap.isMyLocationEnabled = true
                    scheduleMapRecenter()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        initMapCamera(googleMap)
        setCustomInfoAdapter()
        setupMarkers()
        googleMap.setOnCameraIdleListener {
            if (dropEnabled) {

                locationMarker.tag = -1
                locationMarker.showInfoWindow()

                val position = googleMap.cameraPosition.target
                val zoom = googleMap.cameraPosition.zoom.toInt()

                doAsync {
                    val url = "http://daniloleemes.lib.id/reverse-geocoding/?lat=${position.latitude}&lon=${position.longitude}&zoom=$zoom"
                    val result = URL(url).readText(charset = Charset.defaultCharset())
                    runOnUiThread {
                        val geocodeResult = GsonBuilder().create().fromJson(result, ReverseGeocodeResult::class.java)
                        locationMarker.tag = geocodeResult
                        googleMap.setOnInfoWindowClickListener {
                            toggleMarker()
                            val intent = Intent(this@MainActivity, NovoCadastroActivity::class.java)
                            intent.putExtra(IntentFieldNames.LAT, geocodeResult.lat)
                            intent.putExtra(IntentFieldNames.LON, geocodeResult.lon)
                            startActivity(intent)
                        }
                        locationMarker.showInfoWindow()
                    }
                }

            }
        }
        googleMap.setOnCameraMoveListener {
            if (dropEnabled) {
                locationMarker.position = googleMap.cameraPosition.target
            }
        }
        googleMap.setOnMarkerClickListener(onMarkerClickListener())
        if (AppStatics.currentCameraPosition != null) googleMap.moveCamera(AppStatics.currentCameraPosition!!)
        if (!this.googleMap.isMyLocationEnabled && checkLocationPermission()) {
            this.googleMap.isMyLocationEnabled = true
            scheduleMapRecenter()
        } else {
            setupLocation()
        }

        googleMap.isIndoorEnabled = true
        googleMap.isBuildingsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.setAllGesturesEnabled(true)
    }

    private fun setupViews() {
        drawerToggle = setupDrawerToggle()
        setupToolbar()
        setupDrawerContent(leftSideMenu)
        drawerLayout.addDrawerListener(drawerToggle as DrawerLayout.DrawerListener)
        setupNavigationDrawerHeader()

        leftSideMenu.itemIconTintList = null
        rightSideMenu.itemIconTintList = null

        filterBtn.setOnClickListener { drawerLayout.openDrawer(GravityCompat.END) }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupLocation() {
        if (!checkLocationPermission()) {
            requestLocationPermission()
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        MapsInitializer.initialize(application.applicationContext)
        mapView.getMapAsync(this)
    }

    private fun setupNavigationDrawerHeader() {
        val header = leftSideMenu?.getHeaderView(0)
        val userPicture = header?.findViewById<ImageView>(R.id.userPicture)
        val userName = header?.findViewById<TextView>(R.id.usernameText)
        val userEmail = header?.findViewById<TextView>(R.id.userEmailText)
        header?.setOnClickListener {
            startActivity(PerfilActivity::class.java)
        }

        AppStatics.currentUser?.let {
            userPicture?.loadImg(it.fotos?.firstOrNull()?.url)
            userName?.text = it.nome
            userEmail?.text = it.email
        }
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
    }

    private fun setupDrawerContent(navigationDrawer: NavigationView?) {
        navigationDrawer?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_menu_favoritos -> startActivity(FavoritosActivity::class.java)
                R.id.drawer_menu_minhas_publicacoes -> startActivity(MinhasPublicacoesActivity::class.java)
                R.id.drawer_menu_ranking -> startActivity(RankingActivity::class.java)
                R.id.drawer_menu_ultimos_eventos -> startActivity(UltimosAdicionadosActivity::class.java)
                R.id.drawer_menu_indicar -> indicarApp()
                R.id.drawer_menu_termos -> startActivity(TermosActivity::class.java)
                R.id.drawer_menu_fale_conosco -> startActivity(FaleConoscoActivity::class.java)
            }
            true
        }
    }

    private fun indicarApp() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Dê uma olhada neste app incrível\nPlace To Go\nhttps://play.google.com/store/apps/details?id=br.com.gdgbrasilia.meetup.app\n<link da apple store so disponivel depois da publicacao>")
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.send_to)))
    }

    private fun moveMapCamera(location: Location, it: GoogleMap) {
        val mLocation = LatLng(location.latitude, location.longitude)
        val cameraPosition = CameraUpdateFactory
                .newCameraPosition(CameraPosition.builder()
                        .target(mLocation)
                        .zoom(currentZoom)
                        .tilt(0f)
                        .build())
        it.animateCamera(cameraPosition)

        AppStatics.userCurrentLocation = location
        AppStatics.currentCameraPosition = cameraPosition
    }

    private fun scheduleMapRecenter() {
        Handler().postDelayed({
            if (googleMap.isMyLocationEnabled) {
                googleMap.myLocation?.let {
                    moveMapCamera(it, googleMap)
                }

            }
        }, 3000)
    }

    /**
     * Data/Map configuration
     */
    private fun setupMarkers() {
        estabelecimentoViewModel.fetchEstabelecimentos(googleMap).observe(this, Observer { estabelecimentos ->
            estabelecimentos?.let {
                placeMarkers(it)
            }
        })
        eventoViewModel.fetchEventos(googleMap).observe(this, Observer { eventos ->
            eventos?.let {
                placeMarkers(it)
            }
        })
    }

    private fun placeMarkers(list: List<ViewType>) {
        list.forEach { item ->
            if (item is Estabelecimento) {
                val estabelecimentoLocation = LatLng(item.endereco.latitude, item.endereco.longitude)
                val markerOptions = MarkerOptions()
                        .position(estabelecimentoLocation)
                        .icon(getIconFromFile(this, item.tipoEstabelecimentoID, if (item.visitou) R.drawable.ic_pin_rosa else R.drawable.ic_pin_azul))

                val marker = googleMap.addMarker(markerOptions)
                marker.tag = item
                mapMarkers.add(marker)
            } else if (item is Evento) {
                val estabelecimentoLocation = LatLng(item.endereco.latitude, item.endereco.longitude)
                val markerOptions = MarkerOptions()
                        .position(estabelecimentoLocation)
                        .icon(getIconFromFile(this, item.tipoEventoID, if (item.visitou) R.drawable.ic_pin_rosa else R.drawable.ic_pin_azul))

                val marker = googleMap.addMarker(markerOptions)
                marker.tag = item
                mapMarkers.add(marker)
            }
        }
    }

    override fun onEventoFiltered(filter: FilterMenuEventoFragment.EventoFilter) {
        mapMarkers.forEach { it.remove() }
        mapMarkers.clear()
        eventoViewModel.filterEventos(filter, googleMap)
        drawerLayout.closeDrawers()
        isFilterEnabled = true
        filterBtn.setMarked(true)
    }

    override fun onEstabelecimentoFiltered(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter) {
        mapMarkers.forEach { it.remove() }
        mapMarkers.clear()
        estabelecimentoViewModel.filterEstabelecimentos(filter, googleMap)
        drawerLayout.closeDrawers()
        isFilterEnabled = true
        filterBtn.setMarked(true)
    }

    override fun onFilterCleared() {
        mapMarkers.forEach { it.remove() }
        setupMarkers()
        drawerLayout.closeDrawers()
        isFilterEnabled = false
        filterBtn.setMarked(false)
    }

    /**
     * CENTRALIZA O MAPA NO MARKER CORRESPONDENTE AO ITEM SELECIONADO NA BOTTOMSHEET
     */
    override fun selected(item: ViewType) {
        val lat = (item as? Estabelecimento)?.endereco?.latitude
                ?: (item as Evento).endereco.latitude
        val lon = (item as? Estabelecimento)?.endereco?.longitude
                ?: (item as Evento).endereco.longitude
        val cameraPosition = CameraUpdateFactory
                .newCameraPosition(CameraPosition.builder()
                        .target(LatLng(lat, lon))
                        .zoom(currentZoom)
                        .tilt(0f)
                        .build())
        googleMap.moveCamera(cameraPosition)
    }

    private fun initMapCamera(googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(CameraPosition.builder()
                        .target(LatLng(-15.795361355207872, -47.883143350481994))
                        .zoom(12f)
                        .tilt(0f)
                        .build()))
        googleMap.setPadding(0, 350, 0, 0)
    }

    private fun onMarkerClickListener(): GoogleMap.OnMarkerClickListener {
        return GoogleMap.OnMarkerClickListener {
            if (it.tag != null) {
                if (it.tag is ViewType) {
                    if (dropEnabled) toggleMarker()

                    it.hideInfoWindow()
                    val bottomsheet = BottomsheetLocal()
                    val args = Bundle()
                    val list = mutableListOf<ViewType>()

                    list.addAll(mapMarkers.map { it.tag as ViewType })
                    args.putSerializable(IntentFieldNames.ITEMS, list as ArrayList<ViewType>)

                    val item = it.tag as ViewType
                    args.putSerializable(IntentFieldNames.ITEM_INDEX, list.indexOf(item))

                    bottomsheet.arguments = args
                    bottomsheet.show(supportFragmentManager, "ESTABELECIMENTO_SHEET")
                } else {

                }
            }
            false
        }
    }

    private fun setupFiltro() {
        filtroText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty() && editable.length > 3) {
                    mapMarkers.forEach { it.isVisible = false }
                    mapMarkers
                            .filter {
                                ((it.tag as? Estabelecimento)?.nome
                                        ?: (it.tag as Evento).nome).contains(editable.toString(), ignoreCase = true)
                            }
                            .forEach { it.isVisible = true }
                } else {
                    mapMarkers.forEach { it.isVisible = true }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    /**
     * FAB Listener
     * Ativa a função que dropa o pin para escolher o local
     */
    private var dropEnabled = false
    private lateinit var locationMarker: Marker
    private lateinit var customInfoWindowAdapter: CustomInfoWindowAdapter

    private fun novoEvento(): View.OnClickListener? {
        return View.OnClickListener {
            toggleMarker()
        }
    }

    private fun toggleMarker() {
        dropEnabled = !dropEnabled
        novoEventoFab.setMarked(dropEnabled)

        if (dropEnabled) {
            val markerOptions = MarkerOptions()
                    .position(googleMap.cameraPosition.target)
                    .icon(getBitmap(this, R.drawable.ic_pin))

            locationMarker = googleMap.addMarker(markerOptions)
            locationMarker.showInfoWindow()
            locationMarker.tag = googleMap.cameraPosition.target
        } else {
            locationMarker.remove()
        }
    }

    private fun setCustomInfoAdapter() {
        customInfoWindowAdapter = CustomInfoWindowAdapter(this)
        googleMap.setInfoWindowAdapter(customInfoWindowAdapter)
    }

    private fun getBitmap(context: Context, drawableId: Int, dummy: Boolean = true): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(context.resources, drawableId, null)
                ?: return BitmapDescriptorFactory.defaultMarker()
        val height = if (dummy) 1 else 100
        val width = if (dummy) 1 else 70

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, width, height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun getIconFromFile(context: Context, fileID: String, resID: Int): BitmapDescriptor {
        val storageDir = File("$filesDir")
        val file = File(storageDir, fileID)
        val path = file.absolutePath

        if (file.exists()) {
            val icon = BitmapFactory.decodeFile(path)
            val circledIcon = getCircledIcon(icon)
            val pin = putIconInsidePin(circledIcon, resID)
            return BitmapDescriptorFactory.fromBitmap(pin)
        } else {
            return BitmapDescriptorFactory.defaultMarker()
        }
    }

    private fun putIconInsidePin(icon: Bitmap, resID: Int): Bitmap {
        val layerDrawable = ContextCompat.getDrawable(this, resID)!!
        val bitmapDrawable = Bitmap.createBitmap(layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        layerDrawable.setBounds(0, 0, layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight)
        layerDrawable.draw(Canvas(bitmapDrawable))

        val height = layerDrawable.intrinsicHeight
        val width = layerDrawable.intrinsicWidth

        val rootBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(rootBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(bitmapDrawable, 0f, 0f, paint)
        canvas.drawBitmap(icon, (((width - icon.width) / 2).toFloat()), 10f, paint)


        return Bitmap.createScaledBitmap(rootBitmap, layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight, false)
    }

    private fun getCircledIcon(bitmap: Bitmap): Bitmap {
        val layerDrawable = resources.getDrawable(R.drawable.drawable_circle)
        val bitmapDrawable = Bitmap.createBitmap(layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        layerDrawable.setBounds(0, 0, layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight)
        layerDrawable.draw(Canvas(bitmapDrawable))
        val height = layerDrawable.intrinsicHeight.toFloat()
        val side = (height).toInt()

        val rootBitmap = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(rootBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(bitmapDrawable, 0f, 0f, paint)
        canvas.drawBitmap(bitmap, ((side - bitmap.width) / 2).toFloat(), ((side - bitmap.height) / 2).toFloat(), paint)

        return Bitmap.createScaledBitmap(rootBitmap, 60, 60, false)
    }

}
