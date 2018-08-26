package br.com.gdgbrasilia.meetup.app.business.vo

import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.model.ViewTypes
import java.io.Serializable

data class Estabelecimento(val estabelecimentoID: String,
                           val nome: String,
                           val descricao: String,
                           val telefone: String,
                           val email: String,
                           val abreFeriado: Boolean = false,
                           val aceitaPet: Boolean = false,
                           var ativo: Boolean = false,
                           val cnpj: String?,
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
                           val usuarioID: String,
                           var favorito: Boolean = false,
                           var visitou: Boolean = false,
                           val usuario: Usuario,
                           val tipoEstabelecimentoID: String,
                           val tipoEstabelecimento: TipoEstabelecimento,
                           val endereco: Endereco,
                           val fotos: List<AppFoto>,
                           val horarios: List<Horario>) : Serializable, ViewType {

    override fun getViewType(): Int = ViewTypes.ESTABELECIMENTO

}