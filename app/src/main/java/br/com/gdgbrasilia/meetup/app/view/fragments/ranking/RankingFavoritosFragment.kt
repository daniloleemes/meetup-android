package br.com.gdgbrasilia.meetup.app.view.fragments.ranking

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
import br.com.gdgbrasilia.meetup.app.view.adapters.RankingFavoritadosAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.RankingViewModel
import kotlinx.android.synthetic.main.fragment_ranking_favoritos.*
import kotlinx.android.synthetic.main.layout_empty_collection.*

class RankingFavoritosFragment : Fragment() {

    private val adapter by lazy { RankingFavoritadosAdapter(mutableListOf(), activity!!) }
    private val rankingViewModel by lazy { getActivityViewModel(RankingViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_ranking_favoritos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = getDefaultParams()
        params["ordenacaoOrdem"] = "DESC"
        params["ordenacaoCampo"] = "qtdFavoritos"

        swipeLayout.setOnRefreshListener {
            rankingViewModel.fetchEstabelecimentosEventos(params)
        }

        rankingViewModel.fetchEstabelecimentosEventos(params)

        observeList()

        rankingFavoritosRecycler.adapter = adapter
        rankingFavoritosRecycler.layoutManager = LinearLayoutManager(activity)
    }

    fun observeList() {
        rankingViewModel.list.observe(this, Observer { lista ->
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