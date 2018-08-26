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
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsHour
import br.com.gdgbrasilia.meetup.app.util.extensions.formatSite
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_sobre_estabelecimento.*

class EstabelecimentoSobreFragment : Fragment() {

    lateinit var estabelecimento: Estabelecimento
    private val weekDays = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_sobre_estabelecimento)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estabelecimento = arguments!!.getSerializable(IntentFieldNames.ESTABELECIMENTO) as Estabelecimento

        setupViews()
    }

    private fun setupViews() {
        estabelecimentoDescricao.text = estabelecimento.descricao
        entradaText.text = if (estabelecimento.valorEntrada > 0) "Paga" else "Gratuito"
        feriadoText.text = if (estabelecimento.abreFeriado) "Aberto" else "Fechado"
        petText.text = if (estabelecimento.aceitaPet) "Permitido" else "Proibido"

        if (estabelecimento.horarios.any { it.horaAbertura > 0 && it.horaFechamento > 0 }) {
            horarioFuncionamentoText.text = estabelecimento.horarios
                    .sortedBy { it.diaDaSemana }
                    .filter { it.horaAbertura > 0 && it.horaFechamento > 0 }
                    .joinToString(separator = "\n") {
                        "${weekDays[it.diaDaSemana - 1]}:\t${it.horaAbertura.displayAsHour()} - ${it.horaFechamento.displayAsHour()}"
                    }
        } else {
            horarioFuncionamentoText.text = "Horário não informado"
        }

        if (estabelecimento.facebook != null && estabelecimento.facebook!!.isNotEmpty()) {
            facebookBtn.visible().setOnClickListener {
                openSite(estabelecimento.facebook)
            }
        }

        if (estabelecimento.twitter != null && estabelecimento.twitter!!.isNotEmpty()) {
            twitterBtn.visible().setOnClickListener {
                openSite(estabelecimento.twitter)
            }
        }

        if (estabelecimento.youtube != null && estabelecimento.youtube!!.isNotEmpty()) {
            youtubeBtn.visible().setOnClickListener {
                openSite(estabelecimento.youtube)
            }
        }

        if (estabelecimento.instagram != null && estabelecimento.instagram!!.isNotEmpty()) {
            instagramBtn.visible().setOnClickListener {
                openSite(estabelecimento.instagram)
            }
        }
    }

    private fun openSite(site: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(site?.formatSite()))
        startActivity(browserIntent)
    }

}