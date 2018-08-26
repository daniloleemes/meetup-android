package br.com.gdgbrasilia.meetup.app.business.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Horario(val eventoHorarioID: String,
                   var data: String,
                   var diaDaSemana: Int,
                   var horaAbertura: Long,
                   var horaFechamento: Long
) : Serializable

data class HoraMinuto(@SerializedName("Hours") val hours: Int, @SerializedName("Minutes") val minutes: Int) : Serializable