package br.com.gdgbrasilia.meetup.app.model

import java.io.Serializable

/**
 * Created by danilolemes on 28/02/2018.
 */
data class AppResponse(
        val results: List<Any>?,
        val id: Int,
        val backdrops: List<MovieImage>?,
        val posters: List<MovieImage>?,
        val genres: List<Genre>?
) : Serializable