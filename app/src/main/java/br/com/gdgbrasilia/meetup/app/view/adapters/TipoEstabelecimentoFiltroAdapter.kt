package br.com.gdgbrasilia.meetup.app.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.vo.MarkerType
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.viewholder.TipoEstabelecimentoFiltroViewHolder

class TipoEstabelecimentoFiltroAdapter(val list: List<MarkerType>) : RecyclerView.Adapter<TipoEstabelecimentoFiltroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoEstabelecimentoFiltroViewHolder =
            TipoEstabelecimentoFiltroViewHolder(parent.inflate(R.layout.holder_tipo_estabelecimento_filtro))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TipoEstabelecimentoFiltroViewHolder, position: Int) = holder.bind(list[position])

}