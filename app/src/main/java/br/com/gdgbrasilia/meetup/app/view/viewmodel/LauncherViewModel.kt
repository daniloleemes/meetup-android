package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.arch.lifecycle.ViewModel
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.service.MovieService
import br.com.gdgbrasilia.meetup.app.model.Genre
import javax.inject.Inject

/**
 * Created by danilolemes on 01/03/2018.
 */
class LauncherViewModel : ViewModel() {

    @Inject
    lateinit var movieService: MovieService

    init {
        RepositoryComponent.inject(this)
    }

    fun fetchGenres(listener: (List<Genre>?) -> Unit) = movieService.fetchGenres(listener)


}