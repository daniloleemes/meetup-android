package br.com.gdgbrasilia.meetup.app.view.adapters.delegates

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate

class AddFotoDelegateAdapter(val clickListener: View.OnClickListener) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = AddBookViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {}

    inner class AddBookViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.holder_local_picture_add)) {
        init {
            super.itemView.setOnClickListener(clickListener)
        }
    }

}