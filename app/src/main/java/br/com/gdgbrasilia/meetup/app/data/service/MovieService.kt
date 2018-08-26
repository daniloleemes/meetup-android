@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package br.com.gdgbrasilia.meetup.app.data.service

import br.com.gdgbrasilia.meetup.app.business.vo.Genre
import br.com.gdgbrasilia.meetup.app.business.vo.Movie
import br.com.gdgbrasilia.meetup.app.business.vo.MovieImage
import br.com.gdgbrasilia.meetup.app.business.vo.Video
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.repository.MovieRepository
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import br.com.gdgbrasilia.meetup.app.data.util.HttpUtils.Companion.callback
import br.com.gdgbrasilia.meetup.app.data.util.HttpUtils.Companion.parseResponse
import javax.inject.Inject

/**
 * Created by danilolemes on 28/02/2018.
 */
class MovieService {

    @Inject
    lateinit var movieRepository: MovieRepository

    @Inject
    lateinit var dataParser: DataParser

    init {
        RepositoryComponent.inject(this)
    }


    fun fetchNowPlaying(listener: (List<Movie>?) -> Unit) {
        movieRepository.fetchNowPlaying().enqueue(parseResponse { appResponse, throwable ->

        })
    }

    fun fetchUpcoming(page: Int = 1, listener: (List<Movie>?) -> Unit) {
        movieRepository.fetchUpcoming(page).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.results != null) {
                    listener(dataParser.parseList(it.results, Movie::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchGallery(movieID: Int, listener: (List<MovieImage>?) -> Unit) {
        movieRepository.fetchGallery(movieID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let { listener(it.backdrops) }
        })
    }

    fun fetchGenres(listener: (List<Genre>?) -> Unit) {
        movieRepository.fetchGenres().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let { listener(it.genres) }
        })
    }

    fun fetchRecommendations(movieID: Int, listener: (List<Movie>?) -> Unit) {
        movieRepository.fetchRecommendations(movieID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.results != null) {
                    listener(dataParser.parseList(it.results, Movie::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchDetails(movieID: Int, listener: (Movie?, Throwable?) -> Unit) {
        movieRepository.fetchDetails(movieID).enqueue(callback(
                { response ->
                    if (response.body() != null) listener(response.body(), null) else listener(null, Throwable(response.errorBody()?.string()))
                },
                { throwable -> listener(null, throwable) }))
    }

    fun fetchByGenre(genreID: Int, listener: (List<Movie>?) -> Unit) {
        movieRepository.fetchByGenre(genreID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.results != null) {
                    listener(dataParser.parseList(it.results, Movie::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchVideos(movieID: Int, listener: (List<Video>?) -> Unit) {
        movieRepository.fetchVideos(movieID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.results != null) {
                    listener(dataParser.parseList(it.results, Video::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun search(query: String, page: Int = 1, listener: (MutableList<Movie>?) -> Unit) {
        movieRepository.search(query, page).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.results != null) {
                    listener(dataParser.parseList(it.results, Movie::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

}
