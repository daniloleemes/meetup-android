package br.com.gdgbrasilia.meetup.app.business.model

import br.com.gdgbrasilia.meetup.app.business.vo.MarkerType

interface NovoEventoListener {
    fun handle(tipoEstabelecimento: MarkerType)
}