package br.com.gdgbrasilia.meetup.app.business.vo

import java.io.Serializable

data class TipoDenuncia(val tipoDenunciaID: String,
                        val nome: String,
                        val descricao: String) : Serializable