package br.com.gdgbrasilia.meetup.app.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.viewholder.FavoritosViewHolder

class FavoritosAdapter(val list: MutableList<ViewType>, val context: Context) : RecyclerView.Adapter<FavoritosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritosViewHolder =
            FavoritosViewHolder(parent.inflate(R.layout.holder_favoritos), context)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FavoritosViewHolder, position: Int) =
            holder.bind(list[position])

    fun addAll(list: List<ViewType>) {
        this.list.addAll(list)
        this.list.sortByDescending {
            (it as? Estabelecimento)?.dataCadastro ?: (it as? Evento)?.dataCadastro
        }
        notifyDataSetChanged()
    }

    fun add(item: ViewType) {
        this.list.add(item)
        notifyDataSetChanged()
    }

    fun clear() {
        this.list.clear()
        notifyDataSetChanged()
    }
}