package br.com.firstsoft.opentheater.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.firstsoft.opentheater.application.AppApplication.Companion.THUMB_PATH
import br.com.firstsoft.opentheater.model.MovieImage
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.holder_movie_gallery.view.*

/**
 * Created by danilolemes on 01/03/2018.
 */
class GalleryViewHolder(view: View, listener: (View.OnClickListener)) : RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener(listener)
    }

    val image = view.galleryImage

    fun bind(movieImage: MovieImage) {
        Glide.with(image).load(THUMB_PATH + movieImage.file_path).into(image)
    }

}