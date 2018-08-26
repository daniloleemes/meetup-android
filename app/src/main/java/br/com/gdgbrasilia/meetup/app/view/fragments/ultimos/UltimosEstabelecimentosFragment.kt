package br.com.gdgbrasilia.meetup.app.view.fragments.ultimos

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.util.extensions.getActivityViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.getDefaultParams
import br.com.gdgbrasilia.meetup.app.util.extensions.gone
import br.com.gdgbrasilia.meetup.app.util.extensions.visible
import br.com.gdgbrasilia.meetup.app.view.activities.lista.UltimosAdicionadosActivity
import br.com.gdgbrasilia.meetup.app.view.adapters.UltimosAdicionadosAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import kotlinx.android.synthetic.main.fragment_default_list.*
import kotlinx.android.synthetic.main.layout_empty_collection.*

class UltimosEstabelecimentosFragment : Fragment() {

    private val estabelecimentoViewModel by lazy { getActivityViewModel(EstabelecimentoViewModel::class.java) }
    private val adapter by lazy { UltimosAdicionadosAdapter(mutableListOf(), activity!!) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_default_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeLayout.setOnRefreshListener {
            fetchList()
        }

        swipeLayout.isRefreshing = true
        fetchList()
        defaultRecycler.adapter = adapter
        defaultRecycler.layoutManager = LinearLayoutManager(activity)
    }

    private fun fetchList() {
        val params = getDefaultParams()
        val bestLocation = (activity as UltimosAdicionadosActivity).getBestLocation()
        params["raio"] = "50000"
        params["latitude"] = bestLocation?.latitude.toString()
        params["longitude"] = bestLocation?.longitude.toString()

        estabelecimentoViewModel.fetchEstabelecimentos(params).observe(this, Observer { lista ->
            swipeLayout.isRefreshing = false
            if (lista != null) {
                adapter.clear()
                adapter.addAll(lista)
                if (lista.isEmpty()) {
                    emptyLabel.visible()
                } else {
                    emptyLabel.gone()
                }
            } else {
                emptyLabel.visible()
            }
        })
    }

}