package br.com.gdgbrasilia.meetup.app.business.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Avaliacao(
        @SerializedName("estabelecimentoAvaliacaoID", alternate = ["eventoAvaliacaoID"])
        val avaliacaoID: String,
        @SerializedName("estabelecimentoID", alternate = ["eventoID"])
        val localID: String,
        val usuarioID: String,
        val usuario: Usuario,
        val avaliacao: Int,
        val comentario: String,
        val visitou: Boolean,
        val avaliou: Boolean,
        val dataAvaliacao: String?) : Serializable