package br.com.gdgbrasilia.meetup.app.business.vo

import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.model.ViewTypes
import java.io.Serializable

data class Evento(val eventoID: String,
                  val nome: String,
                  val descricao: String,
                  val telefone: String,
                  val email: String,
                  val privado: Boolean,
                  val aceitaPet: Boolean,
                  var ativo: Boolean,
                  val dataAtualizacao: String,
                  val site: String?,
                  val twitter: String? = null,
                  val facebook: String? = null,
                  val instagram: String? = null,
                  val youtube: String? = null,
                  val valorEntrada: Double,
                  val dataCadastro: String,
                  val avaliacao: Double,
                  val qtdFavoritos: Int,
                  val qtdAvaliacoes: Int,
                  var favorito: Boolean = false,
                  var visitou: Boolean = false,
                  val usuarioID: String,
                  val usuario: Usuario,
                  val tipoEventoID: String,
                  val tipoEvento: TipoEvento,
                  val endereco: Endereco,
                  val fotos: List<AppFoto>,
                  val horarios: List<Horario>) : Serializable, ViewType {

    override fun getViewType(): Int = ViewTypes.EVENTO

}