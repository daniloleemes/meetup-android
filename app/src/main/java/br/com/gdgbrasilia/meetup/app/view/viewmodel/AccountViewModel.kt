package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.app.Activity
import android.arch.lifecycle.ViewModel
import br.com.gdgbrasilia.meetup.app.business.vo.MarkerType
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEstabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEvento
import br.com.gdgbrasilia.meetup.app.business.vo.Usuario
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.service.EstabelecimentoService
import br.com.gdgbrasilia.meetup.app.data.service.EventoService
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import mobi.happe.menugo.data.service.HomeService
import javax.inject.Inject

/**
 * Created by danilolemes on 23/12/2017.
 */
class AccountViewModel : ViewModel() {

    @Inject
    lateinit var homeService: HomeService

    @Inject
    lateinit var estabelecimentoService: EstabelecimentoService

    @Inject
    lateinit var eventoService: EventoService

    @Inject
    lateinit var dataParser: DataParser

    init {
        RepositoryComponent.inject(this)
    }

    fun facebookLogin(token: String, listener: (Usuario?, String?) -> Unit) {
        homeService.facebookLogin(token, listener)
    }

    fun handleGoogleSignInResult(result: GoogleSignInResult, context: Activity) {

    }

    fun excluirUsuario(usuarioID: String, listener: (Boolean) -> Unit) {
        homeService.excluirUsuario(usuarioID, listener)
    }

    fun salvarUsuario(usuario: Usuario, listener: (Boolean) -> Unit) {
        homeService.salvarUsuario(usuario, listener)
    }

    fun googleLogin(token: String) {
//        accountService.oAuthGoogle(token)
    }

    fun fetchProfile(usuarioID: String, listener: (Usuario?) -> Unit) {
        homeService.fetchProfile(usuarioID, listener)
    }

    fun fetchCategorias(listener: (List<MarkerType>) -> Unit) = async(UI) {
        val categorias = mutableListOf<MarkerType>()
        val tiposEstabelecimentoResponse = async(CommonPool) { estabelecimentoService.fetchTiposEstabelecimento().execute().body() }.await()
        val tiposEventoResponse = async(CommonPool) { eventoService.fetchTiposEvento().execute().body() }.await()

        tiposEstabelecimentoResponse?.let {
            if (it.sucesso) categorias.addAll(dataParser.parseList(it.dados.lista, TipoEstabelecimento::class.java))
        }

        tiposEventoResponse?.let {
            if (it.sucesso) categorias.addAll(dataParser.parseList(it.dados.lista, TipoEvento::class.java))
        }

        listener(categorias)
    }

}