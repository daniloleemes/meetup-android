package br.com.gdgbrasilia.meetup.app.business.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by danilolemes on 21/12/2017.
 */
class Endereco @Ignore constructor() : Serializable {

    var id: String = ""
    var enderecoID: String = ""
    var logradouro: String = ""
    var bairro: String = ""
    @SerializedName("CEP", alternate = ["cep"])
    var CEP: String = ""
    var cidadeID: Int = 0
    var complemento: String = ""
    @ColumnInfo(name = "nomeEndereco")
    var nome: String = ""
    var principal: Boolean = false
    var numero: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var estadoID: Int = 0
    var cidadeNome: String = ""
    var estadoNome: String = ""

    constructor(id: String, enderecoID: String, logradouro: String
                , bairro: String, CEP: String, cidadeID: Int
                , complemento: String, nome: String, principal: Boolean
                , numero: String, latitude: Double, longitude: Double
                , estadoID: Int, cidadeNome: String, estadoNome: String) : this() {
        this.id = id
        this.enderecoID = enderecoID
        this.logradouro = logradouro
        this.bairro = bairro
        this.CEP = CEP
        this.cidadeID = cidadeID
        this.complemento = complemento
        this.nome = nome
        this.principal = principal
        this.numero = numero
        this.latitude = latitude
        this.longitude = longitude
        this.estadoID = estadoID
        this.cidadeNome = cidadeNome
        this.estadoNome = estadoNome
    }
}