package br.com.gdgbrasilia.meetup.app.data.di.modules

import br.com.gdgbrasilia.meetup.app.data.di.scopes.UserScope
import br.com.gdgbrasilia.meetup.app.data.repository.MovieRepository
import br.com.gdgbrasilia.meetup.app.data.service.MovieService
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RepositoryModule {

    @Provides
    @UserScope
    fun providesMovieRepository(retrofit: Retrofit) = retrofit.create(MovieRepository::class.java)

    @Provides
    @UserScope
    fun providesMovieService() = MovieService()

    @Provides
    @UserScope
    fun providesDataParser(gson: Gson) = DataParser(gson)
//
//    @Provides
//    @UserScope
//    fun providesProposicoesDao() = AppDatabaseImpl.getInstance().appDatabase.proposicoesDao()

}