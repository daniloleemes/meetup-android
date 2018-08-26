package br.com.gdgbrasilia.meetup.app.view.adapters.delegates

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.business.model.ViewType

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}