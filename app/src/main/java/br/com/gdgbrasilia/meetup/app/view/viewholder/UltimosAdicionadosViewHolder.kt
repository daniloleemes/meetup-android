package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsDate
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import br.com.gdgbrasilia.meetup.app.util.extensions.startActivity
import br.com.gdgbrasilia.meetup.app.view.activities.EstabelecimentoActivity
import br.com.gdgbrasilia.meetup.app.view.activities.EventoActivity
import kotlinx.android.synthetic.main.holder_recentes.view.*
import org.joda.time.Instant
import org.joda.time.Interval

class UltimosAdicionadosViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener {

    val localPicture = view.localPicture
    val localName = view.localName
    val localTipo = view.localTipo
    val localCriacao = view.localCriacao

    var local: ViewType? = null

    init {
        view.setOnClickListener(this)
    }

    fun bind(local: ViewType) {
        this.local = local
        if (local is Estabelecimento) {
            val fotoID = local.fotos.firstOrNull()?.arquivoID
            localPicture.loadImg("$BASE_URL/EstabelecimentoFoto/Download/$fotoID/$fotoID.png")
            localName.text = local.nome
            localTipo.text = local.tipoEstabelecimento.nome
            localCriacao.text = getPastTime(getMillis(local.dataCadastro))
        } else if (local is Evento) {
            val fotoID = local.fotos.firstOrNull()?.arquivoID
            localPicture.loadImg("$BASE_URL/EventoFoto/Download/$fotoID/$fotoID.png")
            localName.text = local.nome
            localTipo.text = local.tipoEvento.nome
            localCriacao.text = getPastTime(getMillis(local.dataCadastro))
        }
    }

    override fun onClick(view: View?) {
        if (local is Estabelecimento) {
            val bundle = Bundle()
            bundle.putSerializable(IntentFieldNames.ESTABELECIMENTO, local as Estabelecimento)
            context.startActivity(EstabelecimentoActivity::class.java, bundle)
        } else if (local is Evento) {
            val bundle = Bundle()
            bundle.putSerializable(IntentFieldNames.EVENTO, local as Evento)
            context.startActivity(EventoActivity::class.java, bundle)
        }
    }

    private fun getMillis(time: String): Long {
        return time.replace("/Date(", "").replace(")/", "").toLong()
    }

    private fun getPastTime(millis: Long): String {
        val interval = Interval(Instant(millis), Instant())

        return if (interval.toDuration().standardMinutes < 60) {
            val minutes = interval.toDuration().standardMinutes
            return "há $minutes minuto${getPlural(minutes)} atrás"
        } else if (interval.toDuration().standardHours < 24) {
            val hours = interval.toDuration().standardHours
            return "há $hours hora${getPlural(hours)} atrás"
        } else if (interval.toDuration().standardDays < 7) {
            val days = interval.toDuration().standardDays
            return "há $days dia${getPlural(days)} atrás"
        } else {
            return millis.displayAsDate()
        }
    }

    private fun getPlural(time: Long): String {
        return if (time > 1) "s" else ""
    }
}