package br.com.gdgbrasilia.meetup.app.view.adapters.delegates

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.vo.LocalPictureDTO
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.viewholder.LocalPictureViewHolder

class LocalPictureDelegateAdapter(val context: Context) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            LocalPictureViewHolder(parent.inflate(R.layout.holder_local_picture), context)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as LocalPictureViewHolder
        holder.bind(item as LocalPictureDTO)
    }

}