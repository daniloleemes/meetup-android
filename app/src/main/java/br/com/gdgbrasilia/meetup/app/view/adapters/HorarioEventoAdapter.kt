package br.com.gdgbrasilia.meetup.app.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.vo.Horario
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.viewholder.HorarioEventoViewHolder

class HorarioEventoAdapter(val context: Context) : RecyclerView.Adapter<HorarioEventoViewHolder>() {


    val list = mutableListOf<Horario>()

    init {
        list.add(Horario("", "", 0, 0L, 0L))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioEventoViewHolder =
            HorarioEventoViewHolder(parent.inflate(R.layout.holder_horario_evento), context)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HorarioEventoViewHolder, position: Int) =
            holder.bind(list[position])

    fun add() {
        list.add(Horario("", "", 0, 0L, 0L))
        notifyDataSetChanged()
    }

    fun addAll(horarios: List<Horario>) {
        list.addAll(horarios)
        notifyDataSetChanged()
    }

    fun remove() {
        list.removeAt(list.size - 1)
        notifyDataSetChanged()
    }

}