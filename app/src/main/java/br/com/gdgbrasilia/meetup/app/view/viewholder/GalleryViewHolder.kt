package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.gdgbrasilia.meetup.app.business.vo.MovieImage
import br.com.gdgbrasilia.meetup.app.data.AppConstants.THUMB_PATH
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
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
        image.loadImg(THUMB_PATH + movieImage.file_path)
    }

}