package br.com.gdgbrasilia.meetup.app.data.di.components

import android.content.SharedPreferences
import br.com.gdgbrasilia.meetup.app.data.di.modules.RepositoryModule
import br.com.gdgbrasilia.meetup.app.data.di.scopes.UserScope
import br.com.gdgbrasilia.meetup.app.data.service.EstabelecimentoService
import br.com.gdgbrasilia.meetup.app.data.service.EventoService
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import br.com.gdgbrasilia.meetup.app.view.viewmodel.*
import com.google.gson.Gson
import dagger.Component
import mobi.happe.menugo.data.service.HomeService


@UserScope
@Component(dependencies = [NetComponent::class], modules = [RepositoryModule::class])
interface RepositoryComponent {

    fun sharedPreferences(): SharedPreferences
    fun gson(): Gson
    fun dataParser(): DataParser

    fun inject(homeService: HomeService)
    fun inject(estabelecimentoService: EstabelecimentoService)
    fun inject(eventoService: EventoService)

    fun inject(accountViewModel: AccountViewModel)
    fun inject(estabelecimentoViewModel: EstabelecimentoViewModel)
    fun inject(eventoViewModel: EventoViewModel)
    fun inject(denunciaViewModel: DenunciaViewModel)
    fun inject(politicaViewModel: PoliticaViewModel)
    fun inject(rankingViewModel: RankingViewModel)

}