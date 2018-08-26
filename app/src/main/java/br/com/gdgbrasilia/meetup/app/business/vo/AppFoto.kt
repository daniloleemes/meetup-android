package br.com.gdgbrasilia.meetup.app.business.vo

import java.io.Serializable

/**
 * Created by danilolemes on 16/03/2018.
 */
data class AppFoto(
        var url: String,
        val url64: String,
        var arquivoID: String,
        val nomeArquivo: String,
        val publico: Boolean,
        val ordem: Int
) : Serializable