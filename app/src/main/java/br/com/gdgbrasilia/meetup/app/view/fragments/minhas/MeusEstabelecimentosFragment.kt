package br.com.gdgbrasilia.meetup.app.view.fragments.minhas

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.extensions.*
import br.com.gdgbrasilia.meetup.app.view.adapters.MinhasPublicacoesAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import kotlinx.android.synthetic.main.fragment_default_list.*
import kotlinx.android.synthetic.main.layout_empty_collection.*
import java.util.*

class MeusEstabelecimentosFragment : Fragment() {

    private val adapter by lazy { MinhasPublicacoesAdapter(mutableListOf(), activity!!, onDeleteListener()) }

    private val estabelecimentoViewModel by lazy { getActivityViewModel(EstabelecimentoViewModel::class.java) }
    private val params = getDefaultParams()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_default_list)

    override fun onResume() {
        super.onResume()
        params["filtros"] = "usuarioID.ToString().Equals(\"${AppStatics.currentUser?.usuarioID}\")"
        fetchList(params)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = getDefaultParams()
        params["filtros"] = "usuarioID.ToString().Equals(\"${AppStatics.currentUser?.usuarioID}\")"

        swipeLayout.setOnRefreshListener {
            fetchList(params)
        }

        swipeLayout.isRefreshing = true
        fetchList(params)
        defaultRecycler.adapter = adapter
        defaultRecycler.layoutManager = LinearLayoutManager(activity)
    }

    fun fetchList(params: HashMap<String, String>) {
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

    fun onDeleteListener(): (Boolean) -> Unit {
        return { success ->
            if (success) {
                swipeLayout.isRefreshing = true
                fetchList(params)
            } else {
                Toast.makeText(activity, "Não foi possível excluir o estabelecimento", Toast.LENGTH_SHORT).show()
            }
        }
    }


}