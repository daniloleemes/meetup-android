package br.com.gdgbrasilia.meetup.app.business.vo

import android.net.Uri
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.model.ViewTypes
import java.io.Serializable

data class EstabelecimentoDTO(val nome: String,
                              val email: String,
                              val descricao: String,
                              val tipoEstabelecimentoID: String,
                              val telefone: String,
                              val valorEntrada: Double,
                              val ativo: Boolean,
                              val aceitaPet: Boolean,
                              val site: String,
                              val twitter: String?,
                              val facebook: String?,
                              val instagram: String?,
                              val youtube: String?,
                              val endereco: EnderecoDTO,
                              val horarios: List<HorarioDTO>) : Serializable

data class EventoDTO(val nome: String,
                     val email: String,
                     val descricao: String,
                     val tipoEventoID: String,
                     val telefone: String,
                     val valorEntrada: Double,
                     val ativo: Boolean,
                     val aceitaPet: Boolean,
                     val privado: Boolean,
                     val site: String,
                     val twitter: String?,
                     val facebook: String?,
                     val instagram: String?,
                     val youtube: String?,
                     val endereco: EnderecoDTO,
                     val horarios: List<HorarioDTO>) : Serializable


data class EnderecoDTO(val nome: String,
                       val CEP: String,
                       val logradouro: String,
                       val complemento: String,
                       val numero: String,
                       val estadoID: Int,
                       val cidadeID: Int,
                       val bairro: String,
                       val principal: Boolean,
                       val latitude: Double,
                       val longitude: Double) : Serializable

data class HorarioDTO(var data: String,
                      var diaDaSemana: Int,
                      var horaAbertura: String,
                      var horaFechamento: String) : Serializable

data class LocalPictureDTO(var uri: Uri?) : Serializable, ViewType {
    override fun getViewType(): Int = ViewTypes.PICTURE
}