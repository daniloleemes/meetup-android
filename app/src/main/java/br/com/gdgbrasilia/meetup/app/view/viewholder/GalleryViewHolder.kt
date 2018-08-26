package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.gdgbrasilia.meetup.app.business.vo.AppFoto
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import kotlinx.android.synthetic.main.holder_gallery.view.*

class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val localPicture = view.localPicture

    fun bind(image: AppFoto) {
        localPicture.loadImg("$BASE_URL${image.url}")
    }

}