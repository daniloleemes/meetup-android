package br.com.gdgbrasilia.meetup.app.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.business.vo.MovieImage
import br.com.gdgbrasilia.meetup.app.view.viewholder.GalleryViewHolder

/**
 * Created by danilolemes on 01/03/2018.
 */
class GalleryAdapter(val list: MutableList<MovieImage>, private val listener: View.OnClickListener) : RecyclerView.Adapter<GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder = GalleryViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.holder_movie_gallery, parent, false), listener)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) = holder.bind(list[position])

    fun addAll(list: List<MovieImage>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        this.list.clear()
        notifyDataSetChanged()
    }

}