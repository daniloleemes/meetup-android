package br.com.gdgbrasilia.meetup.app.view.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.model.ViewTypes
import br.com.gdgbrasilia.meetup.app.business.vo.LocalPictureDTO
import br.com.gdgbrasilia.meetup.app.view.adapters.delegates.AddFotoDelegateAdapter
import br.com.gdgbrasilia.meetup.app.view.adapters.delegates.LocalPictureDelegateAdapter
import br.com.gdgbrasilia.meetup.app.view.adapters.delegates.ViewTypeDelegateAdapter

class LocalPictureAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val newPictureItem = object : ViewType {
        override fun getViewType() = ViewTypes.ADD
    }

    init {
        delegateAdapters.put(ViewTypes.PICTURE, LocalPictureDelegateAdapter(context))
        delegateAdapters.put(ViewTypes.ADD, AddFotoDelegateAdapter(this))
        items = ArrayList()
        items.add(newPictureItem)
        addPlaceholders()
    }

    private fun addPlaceholders() {
        items.add(0, LocalPictureDTO(null))
        items.add(0, LocalPictureDTO(null))
        items.add(0, LocalPictureDTO(null))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun onClick(view: View) {
        if (items.size < 9) {
            if (items.size == 7) {
                items.remove(newPictureItem)
                notifyDataSetChanged()
            }
            addPlaceholders()
            notifyDataSetChanged()
        }
    }

}