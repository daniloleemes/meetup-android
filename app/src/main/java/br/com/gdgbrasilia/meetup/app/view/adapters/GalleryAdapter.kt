package br.com.gdgbrasilia.meetup.app.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.vo.AppFoto
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.viewholder.GalleryViewHolder

class GalleryAdapter(val list: List<AppFoto>) : RecyclerView.Adapter<GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder =
            GalleryViewHolder(parent.inflate(R.layout.holder_gallery))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) =
            holder.bind(list[position])

}