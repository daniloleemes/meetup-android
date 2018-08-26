package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.business.vo.*
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.service.EventoService
import br.com.gdgbrasilia.meetup.app.util.extensions.calculateRadius
import br.com.gdgbrasilia.meetup.app.util.extensions.getDefaultParams
import br.com.gdgbrasilia.meetup.app.view.common.ViewTags
import br.com.gdgbrasilia.meetup.app.view.fragments.filter.FilterMenuEventoFragment
import com.google.android.gms.maps.GoogleMap
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt


class EventoViewModel : ViewModel() {

    @Inject
    lateinit var eventoService: EventoService

    var tiposEvento = MutableLiveData<List<TipoEvento>>()
    var eventos = MutableLiveData<List<Evento>>()

    init {
        RepositoryComponent.inject(this)
    }

    fun fetchTiposEvento(): LiveData<List<TipoEvento>> {
        eventoService.fetchTiposEvento { tiposEvento ->
            this.tiposEvento.value = tiposEvento
        }

        return tiposEvento
    }

    fun fetchEventos(googleMap: GoogleMap): LiveData<List<Evento>> {
        val distance = googleMap.calculateRadius().roundToInt()
        val center = googleMap.cameraPosition.target
        val params = getDefaultParams()
        params["raio"] = distance.toString()
        params["latitude"] = center.latitude.toString()
        params["longitude"] = center.longitude.toString()

        eventoService.fetchEventos(params) { eventos ->
            this.eventos.value = eventos
        }

        return eventos
    }

    fun fetchEventos(params: HashMap<String, String> = getDefaultParams()): LiveData<List<Evento>> {
        eventoService.fetchEventos(params) { eventos ->
            this.eventos.value = eventos
        }

        return eventos
    }

    fun filterEventos(filter: FilterMenuEventoFragment.EventoFilter, googleMap: GoogleMap) {
        val distance = googleMap.calculateRadius().roundToInt()
        val center = googleMap.cameraPosition.target
        val params = getDefaultParams()
        params["raio"] = distance.toString()
        params["latitude"] = center.latitude.toString()
        params["longitude"] = center.longitude.toString()
        params["filtros"] = getFiltros(filter)

        eventoService.fetchEventos(params) { eventos ->
            this.eventos.value = eventos
        }
    }

    fun avaliar(eventoID: String, avaliacao: Int, comentario: String, listener: (Boolean) -> Unit) {
        eventoService.avaliar(eventoID, avaliacao, comentario, listener)
    }

    fun fetchAvaliacoes(eventoID: String, listener: (List<Avaliacao>?) -> Unit) {
        eventoService.fetchAvaliacoes(eventoID, listener)
    }

    fun saveEvento(activity: AppCompatActivity, evento: EventoDTO, fotos: List<LocalPictureDTO>, listener: (Boolean) -> Unit) {
        eventoService.saveEvento(activity, evento, fotos, listener)
    }

    fun saveEvento(evento: Evento, listener: (Boolean) -> Unit) {
        eventoService.saveEvento(evento, listener)
    }

    fun excluir(eventoID: String, listener: (Boolean) -> Unit) {
        eventoService.excluir(eventoID, listener)
    }

    private fun getFiltros(filter: FilterMenuEventoFragment.EventoFilter): String {
        val filters = listOf(calculateDate(filter).joinToString(separator = " || "), getTiposEventos(filter), getAceitaPet(filter), getEntrada(filter))
        return filters.filter { it.isNotEmpty() }.joinToString(separator = " && ")
    }

    private fun getTiposEventos(filter: FilterMenuEventoFragment.EventoFilter): String {
        return filter.checkedItems.map { "tipoEventoID.ToString().Equals(\"${it.tipoEventoID}\")" }.joinToString(separator = " || ")
    }

    private fun getEntrada(filter: FilterMenuEventoFragment.EventoFilter): String {
        return if (filter.entrada == 1) {
            ""
        } else if (filter.entrada == 0) {
            "valorEntrada > 0"
        } else {
            "valorEntrada == 0"
        }
    }

    private fun getAceitaPet(filter: FilterMenuEventoFragment.EventoFilter): String {
        return if (filter.pet == 1) {
            ""
        } else if (filter.pet == 0) {
            "aceitaPet == false"
        } else {
            "aceitaPet == true"
        }
    }

    private fun calculateDate(filter: FilterMenuEventoFragment.EventoFilter): List<String> {
        return filter.data.map {
            it.let {
                when (it) {
                    ViewTags.TODAY -> getTodayTime()
                    ViewTags.TOMORROW -> getTomorrowTime()
                    ViewTags.WEEK -> getWeekTime()
                    ViewTags.WEEKEND -> getWeekendTime()
                    else -> ""
                }
            }
        }
    }

    private fun getTodayTime(): String {
        val time = LocalDate.now()
        return "horarios.Any(data == DateTime(${time.year},${time.monthOfYear},${time.dayOfMonth}))"
    }

    private fun getTomorrowTime(): String {
        val time = LocalDate.now().plusDays(1)
        return "horarios.Any(data == DateTime(${time.year},${time.monthOfYear},${time.dayOfMonth}))"
    }

    private fun getWeekTime(): String {
        val now = LocalDate.now()
        val sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY).apply { if (this.isBefore(now)) this.plusWeeks(1) }

        val sundayDay = sunday.dayOfMonth
        val sundayMonth = sunday.monthOfYear
        val sundayYear = sunday.year

        return "(horarios.Any(data >= DateTime(${now.year},${now.monthOfYear},${now.dayOfMonth}) && data <= DateTime($sundayYear,$sundayMonth,$sundayDay))"
    }

    private fun getWeekendTime(): String {
        val now = LocalDate.now()
        val friday = now.withDayOfWeek(DateTimeConstants.FRIDAY)
        val sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY).apply { if (this.isBefore(now)) this.plusWeeks(1) }

        val fridayDay = friday.dayOfMonth
        val fridayMonth = friday.monthOfYear
        val fridayYear = friday.year

        val sundayDay = sunday.dayOfMonth
        val sundayMonth = sunday.monthOfYear
        val sundayYear = sunday.year

        return "(horarios.Any(data >= DateTime($fridayYear,$fridayMonth,$fridayDay) && data <= DateTime($sundayYear,$sundayMonth,$sundayDay)))"
    }

}