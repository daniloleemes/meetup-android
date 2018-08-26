package br.com.gdgbrasilia.meetup.app.business.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by danilolemes on 16/03/2018.
 */
data class AppResponse(
        val id: String,
        val usuarioID: String,
        val mensagem: String?,
        @SerializedName(value = "sucesso", alternate = ["success"])
        val sucesso: Boolean,
        val dados: AppResponseDados,
        @SerializedName(value = "usuaro", alternate = ["usuario", "model"])
        val usuario: Usuario,
        val endereco: Endereco
) : Serializable