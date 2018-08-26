package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.service.EstabelecimentoService
import br.com.gdgbrasilia.meetup.app.data.service.EventoService
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class RankingViewModel : ViewModel() {

    @Inject
    lateinit var eventoService: EventoService

    @Inject
    lateinit var estabelecimentoService: EstabelecimentoService

    @Inject
    lateinit var dataParser: DataParser

    var list = MutableLiveData<MutableList<ViewType>>()

    init {
        RepositoryComponent.inject(this)
    }

    fun fetchEstabelecimentosEventos(params: HashMap<String, String>) = async(UI) {
        val list = mutableListOf<ViewType>()

        fetchEstabelecimentos(params) { estabelecimentos ->
            list.addAll(estabelecimentos)
        }.await()

        fetchEventos(params) { eventos ->
            list.addAll(eventos)
        }.await()


        this@RankingViewModel.list.value = list
    }

    fun fetchEstabelecimentos(params: HashMap<String, String>, listener: (List<Estabelecimento>) -> Unit) = async(UI) {
        val response = async(CommonPool) { estabelecimentoService.fetchEstabelecimentos(params).execute() }.await().body()
        if (response != null) {
            listener(dataParser.parseList(response.dados.lista, Estabelecimento::class.java))
        }
    }

    fun fetchEventos(params: HashMap<String, String>, listener: (List<Evento>) -> Unit) = async(UI) {
        val response = async(CommonPool) { eventoService.fetchEventos(params).execute() }.await().body()
        if (response != null) {
            listener(dataParser.parseList(response.dados.lista, Evento::class.java))
        }
    }

}