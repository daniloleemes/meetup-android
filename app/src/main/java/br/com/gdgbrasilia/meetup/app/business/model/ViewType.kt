package br.com.gdgbrasilia.meetup.app.business.model

interface ViewType {
    fun getViewType(): Int
}

object ViewTypes {
    val EVENTO = 1
    val ESTABELECIMENTO = 2
    val PICTURE = 3
    val ADD = 4
}