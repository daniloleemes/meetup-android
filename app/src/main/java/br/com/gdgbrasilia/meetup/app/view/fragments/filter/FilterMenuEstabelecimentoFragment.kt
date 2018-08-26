package br.com.gdgbrasilia.meetup.app.view.fragments.filter

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEstabelecimento
import br.com.gdgbrasilia.meetup.app.util.extensions.getActivityViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.adapters.TipoEstabelecimentoFiltroAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import kotlinx.android.synthetic.main.fragment_filter_menu_estabelecimento.*
import java.io.Serializable

class FilterMenuEstabelecimentoFragment : Fragment() {

    data class EstabelecimentoFilter(val checkedItems: List<TipoEstabelecimento>,
                                     val opened: Boolean,
                                     val entrada: Int,
                                     val visited: Int,
                                     val pet: Int) : Serializable


    private val estabelecimentoViewModel by lazy { getActivityViewModel(EstabelecimentoViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_filter_menu_estabelecimento)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estabelecimentoViewModel.fetchTiposEstabelecimento().observe(this, Observer { tiposEstabelecimento ->
            tiposEstabelecimento?.let {
                tipoEstabelecimentoFiltroRecycler.adapter = TipoEstabelecimentoFiltroAdapter(it)
                tipoEstabelecimentoFiltroRecycler.layoutManager = GridLayoutManager(activity, 3)
            }
        })

        aplicarFiltroBtn.setOnClickListener { validateFilter() }
        limparFiltroBtn.setOnClickListener { clearFilter() }
    }

    private fun clearFilter() {
        (activity as FilterMenuFragment.FilterListener).onFilterCleared()
        (tipoEstabelecimentoFiltroRecycler.adapter as TipoEstabelecimentoFiltroAdapter).list.forEach { (it as TipoEstabelecimento).checked = false }
        tipoEstabelecimentoFiltroRecycler.adapter = TipoEstabelecimentoFiltroAdapter(
                (tipoEstabelecimentoFiltroRecycler.adapter as TipoEstabelecimentoFiltroAdapter).list
        )
    }

    private fun validateFilter() {
        val adapter = tipoEstabelecimentoFiltroRecycler.adapter as TipoEstabelecimentoFiltroAdapter
        val checkedItems = adapter.list
                .filter { it is TipoEstabelecimento }
                .map { it as TipoEstabelecimento }
                .filter { it.checked }

        val abertoAgora = abertoAgoraSwitch.isChecked
        val entrada = entradaSeekBar.progress
        val visited = jaFuiSeekBar.progress
        val aceitaPet = aceitaPetSeekBar.progress

        val filtro = EstabelecimentoFilter(checkedItems, abertoAgora, entrada, visited, aceitaPet)
        (activity as FilterMenuFragment.FilterListener).onEstabelecimentoFiltered(filtro)
    }
}