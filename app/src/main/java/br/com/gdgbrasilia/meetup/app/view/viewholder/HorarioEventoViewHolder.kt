package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TimePicker
import br.com.gdgbrasilia.meetup.app.business.vo.Horario
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsDate
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsHour
import kotlinx.android.synthetic.main.holder_horario_evento.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class HorarioEventoViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    val eventoData = view.eventoData
    val eventoHoraInicio = view.eventoHoraInicio
    val eventoHoraFim = view.eventoHoraFim

    var horario: Horario? = null

    fun bind(horario: Horario) {

        if (horario.horaAbertura > 0) {
            eventoHoraInicio.text = "${horario.horaAbertura.displayAsHour()}"
            eventoHoraFim.text = "${horario.horaFechamento.displayAsHour()}"
            eventoData.text = "${horario.data.displayAsDate()}"
        } else {
            eventoHoraInicio.setOnClickListener { view ->
                pickHour { _, selectedHour, selectedMinutes ->
                    eventoHoraInicio.text = "${String.format("%02d", selectedHour)}:${String.format("%02d", selectedMinutes)}"
                    val minutes = ((if (selectedHour == 0) 24 else selectedHour) * 60) + selectedMinutes
                    horario.horaAbertura = (minutes * 1000).toLong()
                }
            }

            eventoHoraFim.setOnClickListener { view ->
                pickHour { _, selectedHour, selectedMinutes ->
                    eventoHoraFim.text = "${String.format("%02d", selectedHour)}:${String.format("%02d", selectedMinutes)}"
                    val minutes = ((if (selectedHour == 0) 24 else selectedHour) * 60) + selectedMinutes
                    horario.horaFechamento = TimeUnit.MINUTES.toMillis(minutes.toLong())
                }
            }

            eventoData.setOnClickListener { view ->
                pickDate { day, month, year ->
                    eventoData.text = "${String.format("%02d", day)}/${String.format("%02d", month + 1)}/${String.format("%02d", year)}"
                    horario.data = eventoData.text.toString()
                }
            }
        }


        this.horario = horario
    }

    private fun pickDate(listener: (day: Int, month: Int, year: Int) -> Unit) {
        val mcurrentTime = Calendar.getInstance()
        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, day -> listener(day, month, year) },
                mcurrentTime.get(Calendar.YEAR),
                mcurrentTime.get(Calendar.MONTH),
                mcurrentTime.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun pickHour(listener: (timePicker: TimePicker, selectedHour: Int, selectedMinutes: Int) -> Unit) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
            listener(timePicker, selectedHour, selectedMinute)
        }, hour, minute, true)
        mTimePicker.setTitle("Selecione o horário")
        mTimePicker.show()
    }


}