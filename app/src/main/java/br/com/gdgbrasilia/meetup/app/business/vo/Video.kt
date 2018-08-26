package br.com.firstsoft.opentheater.model

import java.io.Serializable

/**
 * Created by danilolemes on 01/03/2018.
 */
data class Video(
        val id: String,
        val type: String,
        val key: String
) : Serializable