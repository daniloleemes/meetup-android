package br.com.gdgbrasilia.meetup.app.business.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by danilolemes on 20/12/2017.
 */
data class Usuario(@SerializedName("id")
                   var usuarioID: String = "",
                   var nome: String = "",
                   var email: String = "",
                   var dataNascimento: String? = "",
                   var telefone: String? = "",
                   @SerializedName("fotoUrl")
                   var picture: String = "",
                   var fotos: MutableList<AppFoto>? = mutableListOf(),
                   var descricao: String = "",
                   var visivelAoFazerCheckIn: Boolean = false,
                   var compartilharLocalizacao: Boolean = false,
                   var recebimentoDeEmails: Boolean = false,
                   var foto: AppFoto? = null,
                   var fotoUltimoEstabelecimento: AppFoto? = null,
                   var interessesIDs: MutableList<String> = mutableListOf()) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Usuario

        if (usuarioID != other.usuarioID) return false

        return true
    }

    override fun hashCode(): Int {
        return usuarioID.hashCode()
    }


}