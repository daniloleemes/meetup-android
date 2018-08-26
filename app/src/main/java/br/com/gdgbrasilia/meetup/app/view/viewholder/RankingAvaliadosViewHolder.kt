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
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import br.com.gdgbrasilia.meetup.app.util.extensions.startActivity
import br.com.gdgbrasilia.meetup.app.view.activities.EstabelecimentoActivity
import br.com.gdgbrasilia.meetup.app.view.activities.EventoActivity
import kotlinx.android.synthetic.main.holder_ranking_avaliados.view.*

class RankingAvaliadosViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener {

    val localPicture = view.localPicture
    val localName = view.localName
    val localTipo = view.localTipo
    val localNota = view.localNota

    var local: ViewType? = null

    init {
        view.setOnClickListener(this)
    }

    fun bind(local: ViewType) {
        this.local = local
        if (local is Estabelecimento) {
            localPicture.loadImg("$BASE_URL${local.fotos.firstOrNull()?.url}")
            localName.text = local.nome
            localTipo.text = local.tipoEstabelecimento.nome
            localNota.text = String.format("%.2f", local.avaliacao)
        } else if (local is Evento) {
            localPicture.loadImg("$BASE_URL${local.fotos.firstOrNull()?.url}")
            localName.text = local.nome
            localTipo.text = local.tipoEvento.nome
            localNota.text = String.format("%.2f", local.avaliacao)
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
}