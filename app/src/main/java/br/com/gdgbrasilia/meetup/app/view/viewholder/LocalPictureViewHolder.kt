package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import br.com.gdgbrasilia.meetup.app.business.vo.LocalPictureDTO
import br.com.gdgbrasilia.meetup.app.util.extensions.gone
import br.com.gdgbrasilia.meetup.app.util.extensions.visible
import kotlinx.android.synthetic.main.holder_local_picture.view.*

interface LocalPictureSelectListener {
    fun onPictureSelected(localPicture: ImageView, localPictureDTO: LocalPictureDTO, localPictureHolder: LocalPictureViewHolder)
}

class LocalPictureViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val localPicture = view.localPicture
    private val placeholderPicture = view.placeholderPicture
    private val removeBtn = view.removeBtn
    var dto: LocalPictureDTO? = null

    init {
        view.setOnClickListener(this)
    }

    fun bind(dto: LocalPictureDTO) {
        this.dto = dto
        if (dto.uri == null) {
            placeholderPicture.visible()
            localPicture.gone()
        } else {
            placeholderPicture.gone()
            localPicture.visible()
            localPicture.setImageURI(dto.uri)
            removeBtn.visible()
            removeBtn.setOnClickListener {
                localPicture.setImageURI(null)
                this.dto?.uri = null
                placeholderPicture.visible()
                localPicture.gone()
                removeBtn.gone()
            }
        }
    }

    override fun onClick(p0: View?) {
        dto?.let {
            (context as LocalPictureSelectListener).onPictureSelected(localPicture, it, this)
        }
    }

}