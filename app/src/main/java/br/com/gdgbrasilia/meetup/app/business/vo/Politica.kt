package br.com.gdgbrasilia.meetup.app.business.vo

import java.io.Serializable

data class Politica(val politicaID: String,
                    val nome: String,
                    val ativo: Boolean,
                    val politica: String,
                    val versao: Int,
                    val publicado: Boolean,
                    val codigoUnificador: String) : Serializable