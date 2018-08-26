package br.com.firstsoft.opentheater.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import br.com.firstsoft.opentheater.application.AppApplication
import br.com.firstsoft.opentheater.model.Movie
import br.com.firstsoft.opentheater.model.MovieImage
import br.com.firstsoft.opentheater.model.Video
import br.com.firstsoft.opentheater.service.MovieService
import br.com.firstsoft.opentheater.util.HttpHelper.Companion.parseList

/**
 * Created by danilolemes on 28/02/2018.
 */
class MovieViewModel : ViewModel() {

    private val movieService by lazy { MovieService() }

    var upcomingMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    var searchResult: MutableLiveData<MutableList<Movie>> = MutableLiveData()
    var gallery: MutableLiveData<List<MovieImage>> = MutableLiveData()
    var recommendations: MutableLiveData<List<Movie>> = MutableLiveData()
    var movie: MutableLiveData<Movie> = MutableLiveData()
    var movies: MutableLiveData<List<Movie>> = MutableLiveData()
    var video: MutableLiveData<Video> = MutableLiveData()


    fun fetchUpcoming(page: Int = 1): MutableLiveData<List<Movie>> {
        movieService.fetchUpcoming(page) { appResponse, throwable ->
            throwable?.printStackTrace()
            appResponse?.let {
                if (it.results != null) {
                    upcomingMovies.value = parseList(it.results, Movie::class.java)
                } else {
                    Toast.makeText(AppApplication.instance.applicationContext, "Não foi possível realizar a busca. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return upcomingMovies
    }

    fun fetchGallery(movieID: Int): MutableLiveData<List<MovieImage>> {
        movieService.fetchGallery(movieID) { appResponse, throwable ->
            throwable?.printStackTrace()
            appResponse?.let {
                if (it.backdrops != null) {
                    gallery.value = it.backdrops
                }
            }
        }

        return gallery
    }

    fun fetchRecommendations(movieID: Int): MutableLiveData<List<Movie>> {
        movieService.fetchRecommendations(movieID) { appResponse, throwable ->
            throwable?.printStackTrace()
            appResponse?.let {
                it.results?.let {
                    recommendations.value = parseList(it, Movie::class.java)
                }
            }
        }

        return recommendations
    }

    fun fetchDetails(movieID: Int): MutableLiveData<Movie> {
        movieService.fetchDetails(movieID) { movie, throwable ->
            throwable?.printStackTrace()
            movie?.let {
                this.movie.value = it
            }
        }

        return this.movie
    }

    fun fetchByGenre(genreID: Int) {
        movieService.fetchByGenre(genreID) { appResponse, throwable ->
            throwable?.printStackTrace()
            appResponse?.let {
                it.results?.let {
                    movies.value = parseList(it, Movie::class.java)
                }
            }
        }
    }

    fun fetchVideos(movieID: Int): MutableLiveData<Video> {
        movieService.fetchVideos(movieID) { appResponse, throwable ->
            throwable?.printStackTrace()
            appResponse?.let {
                it.results?.let {
                    val list = parseList(it, Video::class.java)
                    video.value = list.first { it.type == "Trailer" }
                }
            }
        }
        return video
    }

    fun search(query: String, progressBar: ProgressBar?, page: Int = 1): MutableLiveData<MutableList<Movie>> {
        progressBar?.visibility = View.VISIBLE
        movieService.search(query, page) { appResponse, throwable ->
            progressBar?.visibility = View.GONE
            throwable?.printStackTrace()
            appResponse?.let {
                it.results?.let {
                    searchResult.value = parseList(it, Movie::class.java)
                }
            }
        }

        return searchResult
    }

}