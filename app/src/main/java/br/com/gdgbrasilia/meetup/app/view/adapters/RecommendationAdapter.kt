package br.com.gdgbrasilia.meetup.app.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.business.vo.Movie
import br.com.gdgbrasilia.meetup.app.view.viewholder.RecommendationViewHolder


/**
 * Created by danilolemes on 28/02/2018.
 */
class RecommendationAdapter(val list: MutableList<Movie>, val context: Context, val listener: (Movie) -> Unit) : RecyclerView.Adapter<RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder = RecommendationViewHolder(LayoutInflater.from(context).inflate(R.layout.holder_movie_playing, parent, false), context, listener)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) = holder.bind(list[position])

    fun addAll(list: List<Movie>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        this.list.clear()
        notifyDataSetChanged()
    }

}