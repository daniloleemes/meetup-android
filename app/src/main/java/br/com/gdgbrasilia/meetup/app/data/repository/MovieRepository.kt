package br.com.firstsoft.opentheater.repository

import br.com.firstsoft.opentheater.model.AppResponse
import br.com.firstsoft.opentheater.model.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by danilolemes on 28/02/2018.
 */
interface MovieRepository {

    @GET("movie/now_playing")
    fun fetchNowPlaying(): Call<AppResponse>

    @GET("movie/upcoming")
    fun fetchUpcoming(@Query("page") page: Int = 1): Call<AppResponse>

    @GET("movie/{movie}/images")
    fun fetchGallery(@Path("movie") movieID: Int): Call<AppResponse>

    @GET("movie/{movie}")
    fun fetchDetails(@Path("movie") movieID: Int): Call<Movie>

    @GET("genre/movie/list")
    fun fetchGenres(): Call<AppResponse>

    @GET("movie/{movie}/recommendations")
    fun fetchRecommendations(@Path("movie") movieID: Int): Call<AppResponse>

    @GET("genre/{genre}/movies")
    fun fetchByGenre(@Path("genre") genreID: Int): Call<AppResponse>

    @GET("movie/{movie}/videos")
    fun fetchVideos(@Path("movie") movieID: Int): Call<AppResponse>

    @GET("search/movie")
    fun search(@Query("query") query: String, @Query("page") page: Int = 1): Call<AppResponse>

}