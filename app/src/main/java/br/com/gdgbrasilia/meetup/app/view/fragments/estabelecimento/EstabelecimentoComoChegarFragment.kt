package br.com.gdgbrasilia.meetup.app.view.fragments.estabelecimento

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_como_chegar_estabelecimento.*

class EstabelecimentoComoChegarFragment : Fragment(), OnMapReadyCallback {

    lateinit var estabelecimento: Estabelecimento

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_como_chegar_estabelecimento)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estabelecimento = arguments!!.getSerializable(IntentFieldNames.ESTABELECIMENTO) as Estabelecimento

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
        estabelecimentoContato.text = estabelecimento.email
        estabelecimentoFone.text = estabelecimento.telefone
        estabelecimentoEndereco.text = "${estabelecimento.endereco.logradouro}, ${estabelecimento.endereco.cidadeNome}, ${estabelecimento.endereco.bairro}, ${estabelecimento.endereco.estadoNome}"
        ligarBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${estabelecimento.telefone
                    .replace("(", "")
                    .replace(")", "")
                    .replace("-", "")
                    .replace(" ", "")}")
            startActivity(intent)
        }
        rotaBtn.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=${estabelecimento.endereco.latitude},${estabelecimento.endereco.longitude}"))
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


        val mLocation = LatLng(estabelecimento.endereco.latitude, estabelecimento.endereco.longitude)
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