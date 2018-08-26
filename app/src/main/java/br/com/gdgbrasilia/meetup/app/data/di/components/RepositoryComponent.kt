package br.com.gdgbrasilia.meetup.app.data.di.components

import android.content.SharedPreferences
import br.com.gdgbrasilia.meetup.app.data.di.modules.RepositoryModule
import br.com.gdgbrasilia.meetup.app.data.di.scopes.UserScope
import br.com.gdgbrasilia.meetup.app.data.service.MovieService
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import br.com.gdgbrasilia.meetup.app.view.viewmodel.LauncherViewModel
import com.google.gson.Gson
import dagger.Component


@UserScope
@Component(dependencies = [NetComponent::class], modules = [RepositoryModule::class])
interface RepositoryComponent {

    fun sharedPreferences(): SharedPreferences
    fun gson(): Gson
    fun dataParser(): DataParser

    fun inject(movieService: MovieService)

    fun inject(movieService: LauncherViewModel)
}