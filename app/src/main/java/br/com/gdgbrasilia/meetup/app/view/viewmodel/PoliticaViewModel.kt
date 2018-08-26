package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.arch.lifecycle.ViewModel
import br.com.gdgbrasilia.meetup.app.business.vo.Politica
import br.com.gdgbrasilia.meetup.app.data.AppApplication
import mobi.happe.menugo.data.service.HomeService
import javax.inject.Inject

class PoliticaViewModel : ViewModel() {

    @Inject
    lateinit var homeService: HomeService

    init {
        AppApplication.RepositoryComponent.inject(this)
    }

    fun fetchDiretrizesUso(listener: (List<Politica>) -> Unit) {
        homeService.fetchPoliticas { politicas ->
            politicas?.let {
                listener(it.filter { it.codigoUnificador == "0b8a510c-3f0e-4267-a177-d80f654ba54d" && it.ativo })
            }
        }
    }

    fun fetchPoliticaPrivacidade(listener: (List<Politica>) -> Unit) {
        homeService.fetchPoliticas { politicas ->
            politicas?.let {
                listener(it.filter { it.codigoUnificador == "c92069b8-bf59-4bfa-8c45-8684421dfaef" && it.ativo })
            }
        }
    }

    fun fetchTermos(listener: (List<Politica>) -> Unit) {
        homeService.fetchPoliticas { politicas ->
            politicas?.let {
                listener(it.filter { it.codigoUnificador == "b69408e8-983f-42bc-9ad0-073b1272fd41" && it.ativo })
            }
        }
    }

}