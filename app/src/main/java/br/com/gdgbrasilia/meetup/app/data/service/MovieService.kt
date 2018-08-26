@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package br.com.gdgbrasilia.meetup.app.data.service

import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.repository.MovieRepository
import br.com.gdgbrasilia.meetup.app.data.util.HttpUtils.Companion.callback
import br.com.gdgbrasilia.meetup.app.data.util.HttpUtils.Companion.parseResponse
import br.com.gdgbrasilia.meetup.app.model.AppResponse
import br.com.gdgbrasilia.meetup.app.model.Genre
import br.com.gdgbrasilia.meetup.app.model.Movie
import javax.inject.Inject

/**
 * Created by danilolemes on 28/02/2018.
 */
class MovieService {

    @Inject
    lateinit var movieRepository: MovieRepository

    init {
        RepositoryComponent.inject(this)
    }


    fun fetchNowPlaying(listener: (AppResponse?, Throwable?) -> Unit) {
        movieRepository.fetchNowPlaying().enqueue(parseResponse { appResponse, throwable ->

        })
    }

    fun fetchUpcoming(page: Int = 1, listener: (AppResponse?, Throwable?) -> Unit) {
        movieRepository.fetchUpcoming(page).enqueue(parseResponse { appResponse, throwable ->

        })
    }

    fun fetchGallery(movieID: Int, listener: (AppResponse?, Throwable?) -> Unit) {
        movieRepository.fetchGallery(movieID).enqueue(parseResponse { appResponse, throwable ->

        })
    }

    fun fetchGenres(listener: (List<Genre>?) -> Unit) {
        movieRepository.fetchGenres().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let { listener(it.genres) }
        })
    }

    fun fetchRecommendations(movieID: Int, listener: (AppResponse?, Throwable?) -> Unit) {
        movieRepository.fetchRecommendations(movieID).enqueue(parseResponse { appResponse, throwable ->

        })
    }

    fun fetchDetails(movieID: Int, listener: (Movie?, Throwable?) -> Unit) {
        movieRepository.fetchDetails(movieID).enqueue(callback(
                { response ->
                    if (response.body() != null) listener(response.body(), null) else listener(null, Throwable(response.errorBody()?.string()))
                },
                { throwable -> listener(null, throwable) }))
    }

    fun fetchByGenre(genreID: Int, listener: (AppResponse?, Throwable?) -> Unit) {
        movieRepository.fetchByGenre(genreID).enqueue(parseResponse { appResponse, throwable ->

        })
    }

    fun fetchVideos(movieID: Int, listener: (AppResponse?, Throwable?) -> Unit) {
        movieRepository.fetchVideos(movieID).enqueue(parseResponse { appResponse, throwable ->

        })
    }

    fun search(query: String, page: Int = 1, listener: (AppResponse?, Throwable?) -> Unit) {
        movieRepository.search(query, page).enqueue(parseResponse { appResponse, throwable ->

        })
    }

}
