package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.gdgbrasilia.meetup.app.business.vo.Movie
import br.com.gdgbrasilia.meetup.app.business.vo.MovieImage
import br.com.gdgbrasilia.meetup.app.business.vo.Video
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.service.MovieService
import javax.inject.Inject

/**
 * Created by danilolemes on 28/02/2018.
 */
class MovieViewModel : ViewModel() {

    @Inject
    lateinit var movieService: MovieService

    init {
        RepositoryComponent.inject(this)
    }

    var upcomingMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    var searchResult: MutableLiveData<MutableList<Movie>> = MutableLiveData()
    var gallery: MutableLiveData<List<MovieImage>> = MutableLiveData()
    var recommendations: MutableLiveData<List<Movie>> = MutableLiveData()
    var movie: MutableLiveData<Movie> = MutableLiveData()
    var movies: MutableLiveData<List<Movie>> = MutableLiveData()
    var video: MutableLiveData<Video> = MutableLiveData()


    fun fetchUpcoming(page: Int = 1): MutableLiveData<List<Movie>> {
        movieService.fetchUpcoming(page) { results ->
            upcomingMovies.value = results
        }
        
        return upcomingMovies
    }

    fun fetchGallery(movieID: Int): MutableLiveData<List<MovieImage>> {
        movieService.fetchGallery(movieID) { backdrops ->
            gallery.value = backdrops
        }

        return gallery
    }

    fun fetchRecommendations(movieID: Int): MutableLiveData<List<Movie>> {
        movieService.fetchRecommendations(movieID) { results ->
            recommendations.value = results
        }

        return recommendations
    }

    fun fetchDetails(movieID: Int): MutableLiveData<Movie> {
        movieService.fetchDetails(movieID) { movie, throwable ->
            this.movie.value = movie
        }

        return this.movie
    }

    fun fetchByGenre(genreID: Int) {
        movieService.fetchByGenre(genreID) { results ->
            movies.value = results
        }
    }

    fun fetchVideos(movieID: Int): MutableLiveData<Video> {
        movieService.fetchVideos(movieID) { videos ->
            video.value = videos?.firstOrNull { it.type == "Trailer" }
        }

        return video
    }

    fun search(query: String, page: Int = 1): MutableLiveData<MutableList<Movie>> {
        movieService.search(query, page) { results ->
            searchResult.value = results
        }

        return searchResult
    }

}