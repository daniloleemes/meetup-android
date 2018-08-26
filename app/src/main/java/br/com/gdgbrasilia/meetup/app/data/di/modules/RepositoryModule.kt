package br.com.gdgbrasilia.meetup.app.data.di.modules

import br.com.gdgbrasilia.meetup.app.data.di.scopes.UserScope
import br.com.gdgbrasilia.meetup.app.data.repository.EstabelecimentoRepository
import br.com.gdgbrasilia.meetup.app.data.repository.EventoRepository
import br.com.gdgbrasilia.meetup.app.data.repository.HomeRepository
import br.com.gdgbrasilia.meetup.app.data.service.EstabelecimentoService
import br.com.gdgbrasilia.meetup.app.data.service.EventoService
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import mobi.happe.menugo.data.service.HomeService
import retrofit2.Retrofit

@Module
class RepositoryModule {

    @Provides
    @UserScope
    fun providesHomeRepository(retrofit: Retrofit) = retrofit.create(HomeRepository::class.java)

    @Provides
    @UserScope
    fun providesEstabelecimentoRepository(retrofit: Retrofit) = retrofit.create(EstabelecimentoRepository::class.java)

    @Provides
    @UserScope
    fun providesEventoRepository(retrofit: Retrofit) = retrofit.create(EventoRepository::class.java)

    @Provides
    @UserScope
    fun providesHomeService() = HomeService()

    @Provides
    @UserScope
    fun providesEstabelecimentoService() = EstabelecimentoService()

    @Provides
    @UserScope
    fun providesEventoService() = EventoService()

    @Provides
    @UserScope
    fun providesDataParser(gson: Gson) = DataParser(gson)
//
//    @Provides
//    @UserScope
//    fun providesProposicoesDao() = AppDatabaseImpl.getInstance().appDatabase.proposicoesDao()

}