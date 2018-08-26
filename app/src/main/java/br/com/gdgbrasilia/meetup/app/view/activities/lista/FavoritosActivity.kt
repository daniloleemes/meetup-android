package br.com.gdgbrasilia.meetup.app.view.activities.lista

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.util.extensions.getDefaultParams
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.gone
import br.com.gdgbrasilia.meetup.app.util.extensions.visible
import br.com.gdgbrasilia.meetup.app.view.adapters.FavoritosAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EventoViewModel
import kotlinx.android.synthetic.main.activity_favoritos.*
import kotlinx.android.synthetic.main.layout_empty_collection.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*

class FavoritosActivity : AppCompatActivity() {

    private val adapter by lazy { FavoritosAdapter(mutableListOf(), this) }
    private val estabelecimentoViewModel by lazy { getViewModel(EstabelecimentoViewModel::class.java) }
    private val eventoViewModel by lazy { getViewModel(EventoViewModel::class.java) }
    private var list = mutableListOf<ViewType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)
        setupToolbar()

        val params = getDefaultParams()
        params["filtros"] = "favorito == true"

        swipeLayout.setOnRefreshListener {
            list.clear()
            fetchList(params)
        }

        observeList()
        fetchList(params)

        favoritosRecycler.adapter = adapter
        favoritosRecycler.layoutManager = LinearLayoutManager(this)
    }

    fun fetchList(params: HashMap<String, String>) {
        estabelecimentoViewModel.fetchEstabelecimentos(params)
        eventoViewModel.fetchEventos(params)
    }

    fun observeList() {
        estabelecimentoViewModel.estabelecimentos.observe(this, Observer { lista ->
            swipeLayout.isRefreshing = false
            if (lista != null) {
                list.addAll(lista)
                adapter.clear()
                adapter.addAll(list)
                if (this.list.isEmpty()) {
                    emptyLabel.visible()
                } else {
                    emptyLabel.gone()
                }
            } else {
                emptyLabel.visible()
            }
        })

        eventoViewModel.eventos.observe(this, Observer { lista ->
            swipeLayout.isRefreshing = false
            if (lista != null) {
                list.addAll(lista)
                adapter.clear()
                adapter.addAll(list)
                if (this.list.isEmpty()) {
                    emptyLabel.visible()
                } else {
                    emptyLabel.gone()
                }
            } else {
                emptyLabel.visible()
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Favoritos"
    }
}
