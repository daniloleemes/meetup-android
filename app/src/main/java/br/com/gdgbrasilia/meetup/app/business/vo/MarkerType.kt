package br.com.gdgbrasilia.meetup.app.business.vo

import java.io.Serializable

interface MarkerType {
    fun getType(): Int
}

object MarkerTypes {
    val EVENTO = 1
    val ESTABELECIMENTO = 2
}

data class TipoEstabelecimento(val tipoEstabelecimentoID: String,
                               val nome: String,
                               val descricao: String?,
                               val cor: String?,
                               val iconeArquivoID: String?,
                               val iconeArquivo: IconeArquivo?,
                               var checked: Boolean = false) : Serializable, MarkerType {

    override fun getType(): Int = MarkerTypes.ESTABELECIMENTO
}

data class TipoEvento(val tipoEventoID: String,
                      val nome: String,
                      val cor: String?,
                      val descricao: String?,
                      val iconeArquivoID: String?,
                      val iconeArquivo: IconeArquivo?,
                      var checked: Boolean = false) : Serializable, MarkerType {

    override fun getType(): Int = MarkerTypes.EVENTO
}

data class IconeArquivo(val iconeArquivoID: String,
                        val nomeArquivo: String,
                        val arquivoID: String,
                        val tipo: String) : Serializable