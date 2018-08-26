package br.com.gdgbrasilia.meetup.app.view.fragments.filter

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEvento
import br.com.gdgbrasilia.meetup.app.util.extensions.getActivityViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.util.extensions.viewsRecursive
import br.com.gdgbrasilia.meetup.app.view.adapters.TipoEstabelecimentoFiltroAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EventoViewModel
import kotlinx.android.synthetic.main.fragment_filter_menu_evento.*
import java.io.Serializable

class FilterMenuEventoFragment : Fragment() {

    data class EventoFilter(val checkedItems: List<TipoEvento>,
                            val data: List<String>,
                            val entrada: Int,
                            val pet: Int) : Serializable

    private val eventoViewModel by lazy { getActivityViewModel(EventoViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_filter_menu_evento)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventoViewModel.fetchTiposEvento().observe(this, Observer { tiposEstabelecimento ->
            tiposEstabelecimento?.let {
                tipoEventoFiltroRecycler.adapter = TipoEstabelecimentoFiltroAdapter(it)
                tipoEventoFiltroRecycler.layoutManager = GridLayoutManager(activity, 3)
            }
        })

        aplicarFiltroBtn.setOnClickListener { validateFilter() }
        limparFiltroBtn.setOnClickListener { clearFilter() }

    }

    private fun clearFilter() {
        (activity as FilterMenuFragment.FilterListener).onFilterCleared()
        (tipoEventoFiltroRecycler.adapter as TipoEstabelecimentoFiltroAdapter).list.forEach { (it as TipoEvento).checked = false }
        tipoEventoFiltroRecycler.adapter = TipoEstabelecimentoFiltroAdapter(
                (tipoEventoFiltroRecycler.adapter as TipoEstabelecimentoFiltroAdapter).list
        )
    }

    private fun validateFilter() {
        val adapter = tipoEventoFiltroRecycler.adapter as TipoEstabelecimentoFiltroAdapter

        val checkedItems = adapter.list
                .filter { it is TipoEvento }
                .map { it as TipoEvento }
                .filter { it.checked }
        val data = dataCheckboxWrapper.viewsRecursive
                .filter { it is AppCompatCheckBox }
                .map { it as AppCompatCheckBox }
                .filter { it.isChecked }
                .map { it.tag.toString() }
        val entrada = entradaSeekBar.progress
        val aceitaPet = aceitaPetSeekBar.progress

        val filtro = EventoFilter(checkedItems, data, entrada, aceitaPet)
        (activity as FilterMenuFragment.FilterListener).onEventoFiltered(filtro)
    }
}