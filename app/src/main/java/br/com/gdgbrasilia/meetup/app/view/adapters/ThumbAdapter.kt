package br.com.firstsoft.opentheater.adapter

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.firstsoft.opentheater.application.TransitionNames
import br.com.firstsoft.opentheater.model.Movie
import br.com.firstsoft.opentheater.viewholder.ThumbViewHolder


/**
 * Created by danilolemes on 28/02/2018.
 */
class ThumbAdapter(val list: MutableList<Movie>, val context: Context, val resId: Int) : RecyclerView.Adapter<ThumbViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThumbViewHolder = ThumbViewHolder(LayoutInflater.from(context).inflate(resId, parent, false), context)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ThumbViewHolder?, position: Int) {
        holder?.let {
            ViewCompat.setTransitionName(it.poster, "${TransitionNames.POSTER}_$position")
            holder.bind(list[position])
        }
    }

    fun addAll(list: List<Movie>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        this.list.clear()
        notifyDataSetChanged()
    }

}