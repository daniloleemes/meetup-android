package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.gdgbrasilia.meetup.app.business.vo.MarkerType
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEstabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEvento
import br.com.gdgbrasilia.meetup.app.util.extensions.setMarked
import kotlinx.android.synthetic.main.holder_tipo_estabelecimento_filtro.view.*

class TipoEstabelecimentoFiltroViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    val tipoNome = view.tipoNome
    val tipoBackground = view.tipoBackground

    var tipo: MarkerType? = null

    init {
        view.setOnClickListener(this)
    }

    fun bind(tipo: MarkerType) {
        this.tipo = tipo

        if (tipo is TipoEstabelecimento) {
            this.tipoNome.text = tipo.nome
        } else if (tipo is TipoEvento) {
            this.tipoNome.text = tipo.nome
        }

    }

    override fun onClick(view: View?) {
        tipo?.let {
            if (it is TipoEvento) {
                it.checked = !it.checked
                tipoBackground.setMarked(it.checked)
            } else if (it is TipoEstabelecimento) {
                it.checked = !it.checked
                tipoBackground.setMarked(it.checked)
            }
        }
    }

}