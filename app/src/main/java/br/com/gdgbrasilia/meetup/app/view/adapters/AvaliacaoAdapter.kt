package br.com.gdgbrasilia.meetup.app.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.vo.Avaliacao
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.viewholder.AvaliacaoViewHolder

class AvaliacaoAdapter(val list: MutableList<Avaliacao>) : RecyclerView.Adapter<AvaliacaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaliacaoViewHolder =
            AvaliacaoViewHolder(parent.inflate(R.layout.holder_avaliacao))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AvaliacaoViewHolder, position: Int) = holder.bind(list[position])

    fun add(avaliacao: Avaliacao) {
        this.list.add(avaliacao)
        notifyDataSetChanged()
    }
}