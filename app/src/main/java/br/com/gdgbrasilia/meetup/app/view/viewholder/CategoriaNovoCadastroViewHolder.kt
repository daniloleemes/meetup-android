package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.gdgbrasilia.meetup.app.business.model.NovoEventoListener
import br.com.gdgbrasilia.meetup.app.business.vo.MarkerType
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEstabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEvento
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import kotlinx.android.synthetic.main.holder_categoria_novo_cadastro.view.*

class CategoriaNovoCadastroViewHolder(view: View, val context: Context, listener: NovoEventoListener) : RecyclerView.ViewHolder(view) {

    val categoriaIcon = view.categoriaIcon
    val categoriaName = view.categoriaName
    var tipoEstabelecimento: MarkerType? = null

    init {
        view.setOnClickListener {
            tipoEstabelecimento?.let {
                listener.handle(it)
            }
        }
    }

    fun bind(markerType: MarkerType) {
        this.tipoEstabelecimento = markerType
        if (markerType is TipoEstabelecimento) {
            this.categoriaName.text = markerType.nome
            this.categoriaIcon.loadImg("$BASE_URL/TipoEstabelecimento/Download?id=${markerType.tipoEstabelecimentoID}")
        } else if (markerType is TipoEvento) {
            this.categoriaName.text = markerType.nome
            this.categoriaIcon.loadImg("$BASE_URL/TipoEvento/Download?id=${markerType.tipoEventoID}")
        }
    }

}