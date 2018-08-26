package br.com.gdgbrasilia.meetup.app.business.vo

import java.io.Serializable

/**
 * Created by danilolemes on 16/03/2018.
 */
data class AppResponseDados(
        val lista: List<Any>,
        val pagina: Long,
        val indiceInicial: Long,
        val indiceFinal: Long,
        val ultimaPagina: Long,
        val total: Long
) : Serializable