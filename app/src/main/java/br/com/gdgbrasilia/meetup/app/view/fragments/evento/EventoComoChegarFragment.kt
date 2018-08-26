package br.com.gdgbrasilia.meetup.app.view.fragments.evento

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.util.extensions.formatSite
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_como_chegar_evento.*


class EventoComoChegarFragment : Fragment(), OnMapReadyCallback {

    lateinit var evento: Evento

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_como_chegar_evento)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        evento = arguments!!.getSerializable(IntentFieldNames.EVENTO) as Evento

        setupViews()
        setupMap(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        estabelecimentoMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        estabelecimentoMapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        estabelecimentoMapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        estabelecimentoMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        estabelecimentoMapView?.onLowMemory()
    }

    private fun setupViews() {
        estabelecimentoSite.text = evento.site
        estabelecimentoContato.text = evento.email
        estabelecimentoFone.text = evento.telefone
        estabelecimentoEndereco.text = "${evento.endereco.logradouro}, ${evento.endereco.cidadeNome}, ${evento.endereco.bairro}, ${evento.endereco.estadoNome}"
        estabelecimentoContato.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(evento.email))
            startActivity(Intent.createChooser(intent, "Contato"))
        }
        estabelecimentoSite.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(evento.site?.formatSite()))
            startActivity(browserIntent)
        }
        estabelecimentoFone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${evento.telefone
                    .replace("(", "")
                    .replace(")", "")
                    .replace("-", "")
                    .replace(" ", "")}")
            startActivity(intent)
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        estabelecimentoMapView.onCreate(savedInstanceState)
        estabelecimentoMapView.onResume()
        MapsInitializer.initialize(activity!!.application.applicationContext)
        estabelecimentoMapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.setAllGesturesEnabled(false)
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false
        estabelecimentoMapView.isFocusable = false
        val mLocation = LatLng(evento.endereco.latitude, evento.endereco.longitude)
        val cameraPosition = CameraUpdateFactory
                .newCameraPosition(CameraPosition.builder()
                        .target(mLocation)
                        .zoom(14f)
                        .tilt(0f)
                        .build())
        googleMap.animateCamera(cameraPosition)

        val markerOptions = MarkerOptions()
                .position(mLocation)
        googleMap.addMarker(markerOptions)
    }
}