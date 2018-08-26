package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.gdgbrasilia.meetup.app.business.vo.TipoDenuncia
import br.com.gdgbrasilia.meetup.app.data.AppApplication
import br.com.gdgbrasilia.meetup.app.data.service.EstabelecimentoService
import br.com.gdgbrasilia.meetup.app.data.service.EventoService
import mobi.happe.menugo.data.service.HomeService
import javax.inject.Inject

class DenunciaViewModel : ViewModel() {

    @Inject
    lateinit var homeService: HomeService

    @Inject
    lateinit var estabelecimentoService: EstabelecimentoService

    @Inject
    lateinit var eventoService: EventoService

    var tiposDenuncia = MutableLiveData<List<TipoDenuncia>>()

    init {
        AppApplication.RepositoryComponent.inject(this)
    }

    fun fetchTiposDenuncia(): LiveData<List<TipoDenuncia>> {
        homeService.fetchTiposDenuncia { tiposDenuncia ->
            this.tiposDenuncia.value = tiposDenuncia
        }

        return tiposDenuncia
    }

    fun denunciarEstabelecimento(estabelecimentoID: String, tipoDenunciaID: String, denuncia: String, listener: (Boolean) -> Unit) {
        estabelecimentoService.denunciar(estabelecimentoID, tipoDenunciaID, denuncia, listener)
    }

    fun denunciarEvento(eventoID: String, tipoDenunciaID: String, denuncia: String, listener: (Boolean) -> Unit) {
        eventoService.denunciar(eventoID, tipoDenunciaID, denuncia, listener)
    }
}