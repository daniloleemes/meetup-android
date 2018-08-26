package br.com.gdgbrasilia.meetup.app.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.NovoEventoListener
import br.com.gdgbrasilia.meetup.app.business.vo.MarkerType
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.viewholder.CategoriaNovoCadastroViewHolder

class CategoriaNovoCadastroAdapter(val list: List<MarkerType>, val context: Context, private val listener: NovoEventoListener) : RecyclerView.Adapter<CategoriaNovoCadastroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaNovoCadastroViewHolder =
            CategoriaNovoCadastroViewHolder(parent.inflate(R.layout.holder_categoria_novo_cadastro), context, listener)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CategoriaNovoCadastroViewHolder, position: Int) = holder.bind(list[position])

}