@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package br.com.gdgbrasilia.meetup.app.data.service

import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.business.vo.*
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.repository.EventoRepository
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import br.com.gdgbrasilia.meetup.app.data.util.HttpUtils.Companion.parseResponse
import br.com.gdgbrasilia.meetup.app.util.FileUtils
import br.com.gdgbrasilia.meetup.app.util.extensions.getDefaultParams
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

class EventoService {

    @Inject
    lateinit var eventoRepository: EventoRepository

    @Inject
    lateinit var dataParser: DataParser

    init {
        RepositoryComponent.inject(this)
    }

    fun fetchTiposEvento(listener: (List<TipoEvento>?) -> Unit) {
        eventoRepository.fetchTiposEvento().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(dataParser.parseList(it.dados.lista, TipoEvento::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchTiposEvento(): Call<AppResponse> {
        return eventoRepository.fetchTiposEvento()
    }

    fun fetchEventos(params: HashMap<String, String> = getDefaultParams(), listener: (List<Evento>?) -> Unit) {
        eventoRepository.fetchEventos(params).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(dataParser.parseList(it.dados.lista, Evento::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchEventos(params: HashMap<String, String> = getDefaultParams()): Call<AppResponse> {
        return eventoRepository.fetchEventos(params)
    }

    fun saveEvento(activity: AppCompatActivity, evento: EventoDTO, fotos: List<LocalPictureDTO>, listener: (Boolean) -> Unit) {
        val fotosPart = fotos
                .filter { it.uri != null }
                .mapIndexed { index, fotoDTO ->
                    val file = FileUtils.instance.getFile(activity, fotoDTO.uri!!)
                    val requestFile = RequestBody.create(MediaType.parse(activity.contentResolver.getType(fotoDTO.uri!!)), file)
                    MultipartBody.Part.createFormData("fotos_$index", file!!.name, requestFile)
                }

        val eventoJson = Gson().toJson(evento)
        val eventoBody = RequestBody.create(MultipartBody.FORM, eventoJson)
        val eventoPart = MultipartBody.Part.createFormData("dados", null, eventoBody)

        eventoRepository.saveEvento(fotosPart, eventoPart, evento.endereco.latitude, evento.endereco.longitude).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let {
                listener(it.sucesso)
            }
        })
    }

    fun saveEvento(evento: Evento, listener: (Boolean) -> Unit) {
        eventoRepository.saveEvento(evento).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun denunciar(eventoID: String, tipoDenunciaID: String, denuncia: String, listener: (Boolean) -> Unit) {
        val params = HashMap<String, String>()
        params["eventoID"] = eventoID
        params["tipoDenunciaID"] = tipoDenunciaID
        params["descricao"] = denuncia

        eventoRepository.denunciar(params).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun avaliar(eventoID: String, avaliacao: Int, comentario: String, listener: (Boolean) -> Unit) {
        val params = HashMap<String, String>()
        params["eventoID"] = eventoID
        params["avaliacao"] = avaliacao.toString()
        params["comentario"] = comentario

        eventoRepository.avaliar(params).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun fetchAvaliacoes(eventoID: String, listener: (List<Avaliacao>?) -> Unit) {
        val params = getDefaultParams()
        params["filtros"] = "eventoID.ToString().Equals(\"$eventoID\")"
        eventoRepository.fetchAvaliacoes(params).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(dataParser.parseList(it.dados.lista, Avaliacao::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun excluir(eventoID: String, listener: (Boolean) -> Unit) {
        eventoRepository.excluirEvento(eventoID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

}