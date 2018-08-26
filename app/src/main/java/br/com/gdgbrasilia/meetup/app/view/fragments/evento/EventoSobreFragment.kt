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
import br.com.gdgbrasilia.meetup.app.util.extensions.*
import kotlinx.android.synthetic.main.fragment_sobre_evento.*

class EventoSobreFragment : Fragment() {

    lateinit var evento: Evento

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_sobre_evento)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        evento = arguments!!.getSerializable(IntentFieldNames.EVENTO) as Evento

        setupViews()
    }

    private fun setupViews() {
        estabelecimentoDescricao.text = evento.descricao
        entradaText.text = if (evento.valorEntrada > 0) "Paga" else "Gratuito"
        petText.text = if (evento.aceitaPet) "Permitido" else "Proibido"
        horarioFuncionamentoText.text = evento.horarios
                .sortedBy { it.data }
                .joinToString(separator = "\n\n") {
                    "${it.data.displayAsDate()}\n${it.horaAbertura.displayAsHour()} - ${it.horaFechamento.displayAsHour()}"
                }

        if (evento.facebook != null && evento.facebook!!.isNotEmpty()) {
            facebookBtn.visible().setOnClickListener {
                openSite(evento.facebook)
            }
        }

        if (evento.twitter != null && evento.twitter!!.isNotEmpty()) {
            twitterBtn.visible().setOnClickListener {
                openSite(evento.twitter)
            }
        }

        if (evento.youtube != null && evento.youtube!!.isNotEmpty()) {
            youtubeBtn.visible().setOnClickListener {
                openSite(evento.youtube)
            }
        }

        if (evento.instagram != null && evento.youtube!!.isNotEmpty()) {
            instagramBtn.visible().setOnClickListener {
                openSite(evento.instagram)
            }
        }
    }

    private fun openSite(site: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(site?.formatSite()))
        startActivity(browserIntent)
    }

}