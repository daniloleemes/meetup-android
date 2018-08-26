package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.business.vo.*
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.service.EstabelecimentoService
import br.com.gdgbrasilia.meetup.app.util.extensions.calculateRadius
import br.com.gdgbrasilia.meetup.app.util.extensions.getDefaultParams
import br.com.gdgbrasilia.meetup.app.view.fragments.filter.FilterMenuEstabelecimentoFragment
import com.google.android.gms.maps.GoogleMap
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

class EstabelecimentoViewModel : ViewModel() {

    @Inject
    lateinit var estabelecimentoService: EstabelecimentoService

    var tiposEstabelecimento = MutableLiveData<List<TipoEstabelecimento>>()
    var estabelecimentos = MutableLiveData<List<Estabelecimento>>()

    init {
        RepositoryComponent.inject(this)
    }

    fun fetchTiposEstabelecimento(): LiveData<List<TipoEstabelecimento>> {
        estabelecimentoService.fetchTiposEstabelecimento { tiposEstabelecimento ->
            this.tiposEstabelecimento.value = tiposEstabelecimento
        }

        return tiposEstabelecimento
    }

    fun fetchEstabelecimentos(googleMap: GoogleMap): LiveData<List<Estabelecimento>> {
        val distance = googleMap.calculateRadius().roundToInt()
        val center = googleMap.cameraPosition.target
        val params = getDefaultParams()
        params["raio"] = distance.toString()
        params["latitude"] = center.latitude.toString()
        params["longitude"] = center.longitude.toString()

        estabelecimentoService.fetchEstabelecimentos(params) { estabelecimentos ->
            this.estabelecimentos.value = estabelecimentos
        }

        return estabelecimentos
    }

    fun fetchEstabelecimentos(params: HashMap<String, String> = getDefaultParams()): LiveData<List<Estabelecimento>> {
        estabelecimentoService.fetchEstabelecimentos(params) { estabelecimentos ->
            this.estabelecimentos.value = estabelecimentos
        }

        return estabelecimentos
    }

    fun fetchAvaliacoes(estabelecimentoID: String, listener: (List<Avaliacao>?) -> Unit) {
        estabelecimentoService.fetchAvaliacoes(estabelecimentoID, listener)
    }

    fun consultarCEP(cep: String, listener: (Endereco?) -> Unit) {
        estabelecimentoService.consultarCEP(cep, listener)
    }

    fun filterEstabelecimentos(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter, googleMap: GoogleMap) {
        val distance = googleMap.calculateRadius().roundToInt()
        val center = googleMap.cameraPosition.target
        val params = getDefaultParams()
        params["raio"] = distance.toString()
        params["latitude"] = center.latitude.toString()
        params["longitude"] = center.longitude.toString()
        params["filtros"] = getFiltros(filter)

        estabelecimentoService.fetchEstabelecimentos(params) { estabelecimentos ->
            this.estabelecimentos.value = estabelecimentos
        }
    }

    fun saveEstabelecimento(activity: AppCompatActivity, estabelecimento: EstabelecimentoDTO, fotos: List<LocalPictureDTO>, listener: (Boolean) -> Unit) {
        estabelecimentoService.saveEstabelecimento(activity, estabelecimento, fotos, listener)
    }

    fun saveEstabelecimento(estabelecimento: Estabelecimento, listener: (Boolean) -> Unit) {
        estabelecimentoService.saveEstabelecimento(estabelecimento, listener)
    }

    fun marcarFavorito(estabelecimentoID: String, favorito: Boolean, listener: (Boolean) -> Unit) {
        estabelecimentoService.marcarFavorito(estabelecimentoID, favorito, listener)
    }

    fun marcarVisita(estabelecimentoID: String, visitou: Boolean, listener: (Boolean) -> Unit) {
        estabelecimentoService.marcarVisita(estabelecimentoID, visitou, listener)
    }

    fun avaliar(estabelecimentoID: String, avaliacao: Int, comentario: String, listener: (Boolean) -> Unit) {
        estabelecimentoService.avaliar(estabelecimentoID, avaliacao, comentario, listener)
    }

    fun excluir(estabelecimentoID: String, listener: (Boolean) -> Unit) {
        estabelecimentoService.excluir(estabelecimentoID, listener)
    }

    private fun getFiltros(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter): String {
        val filters = listOf(getTiposEstabelecimentos(filter), getAceitaPet(filter), getEntrada(filter), getVisited(filter), getOpened(filter))
        return filters.filter { it.isNotEmpty() }.joinToString(separator = " && ")
    }

    private fun getTiposEstabelecimentos(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter): String {
        return filter.checkedItems.map { "tipoEstabelecimentoID.ToString().Equals(\"${it.tipoEstabelecimentoID}\")" }.joinToString(separator = " || ")
    }

    private fun getVisited(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter): String {
        return when {
            filter.visited == 1 -> ""
            filter.visited == 0 -> "jaVisitei == false"
            else -> "jaVisitei == true"
        }
    }

    private fun getEntrada(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter): String {
        return when {
            filter.entrada == 1 -> ""
            filter.entrada == 0 -> "valorEntrada > 0"
            else -> "valorEntrada == 0"
        }
    }

    private fun getAceitaPet(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter): String {
        return when {
            filter.pet == 1 -> ""
            filter.pet == 0 -> "aceitaPet == false"
            else -> "aceitaPet == true"
        }
    }

    private fun getOpened(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter): String {
        return if (!filter.opened) "" else {
            val time = Date().time
            "horarios.Any(horaAbertura >= $time && horaAbertura <= $time)"
        }
    }

}