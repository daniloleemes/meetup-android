@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package br.com.firstsoft.opentheater.service

import br.com.firstsoft.opentheater.model.AppResponse
import br.com.firstsoft.opentheater.model.Movie
import br.com.firstsoft.opentheater.repository.BaseRepository
import br.com.firstsoft.opentheater.util.HttpHelper.Companion.callback
import br.com.firstsoft.opentheater.util.HttpHelper.Companion.parseResponse

/**
 * Created by danilolemes on 28/02/2018.
 */
class MovieService {

    private val movieRepository by lazy { BaseRepository.createMovieRepository() }

    fun fetchNowPlaying(listener: (AppResponse?, Throwable?) -> Unit) = movieRepository.fetchNowPlaying().enqueue(parseResponse(listener))

    fun fetchUpcoming(page: Int = 1, listener: (AppResponse?, Throwable?) -> Unit) = movieRepository.fetchUpcoming(page).enqueue(parseResponse(listener))

    fun fetchGallery(movieID: Int, listener: (AppResponse?, Throwable?) -> Unit) = movieRepository.fetchGallery(movieID).enqueue(parseResponse(listener))

    fun fetchGenres(listener: (AppResponse?, Throwable?) -> Unit) = movieRepository.fetchGenres().enqueue(parseResponse(listener))

    fun fetchRecommendations(movieID: Int, listener: (AppResponse?, Throwable?) -> Unit) = movieRepository.fetchRecommendations(movieID).enqueue(parseResponse(listener))

    fun fetchDetails(movieID: Int, listener: (Movie?, Throwable?) -> Unit) {
        movieRepository.fetchDetails(movieID).enqueue(callback(
                { response ->
                    if (response.body() != null) listener(response.body(), null) else listener(null, Throwable(response.errorBody()?.string()))
                },
                { throwable -> listener(null, throwable) }))
    }

    fun fetchByGenre(genreID: Int, listener: (AppResponse?, Throwable?) -> Unit) = movieRepository.fetchByGenre(genreID).enqueue(parseResponse(listener))

    fun fetchVideos(movieID: Int, listener: (AppResponse?, Throwable?) -> Unit) = movieRepository.fetchVideos(movieID).enqueue(parseResponse(listener))

    fun search(query: String, page: Int = 1, listener: (AppResponse?, Throwable?) -> Unit) = movieRepository.search(query, page).enqueue(parseResponse(listener))

}
