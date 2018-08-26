package br.com.firstsoft.opentheater.model

import java.io.Serializable

/**
 * Created by danilolemes on 28/02/2018.
 */
data class Movie(
        val vote_count: Int,
        val id: Int,
        val video: Boolean,
        val vote_average: Float,
        val title: String,
        val popularity: Double,
        val poster_path: String,
        val original_language: String,
        val original_title: String,
        val genre_ids: List<Int>,
        val backdrop_path: String,
        val adult: Boolean,
        val overview: String,
        val release_date: String,
        val runtime: Int?
) : Serializable