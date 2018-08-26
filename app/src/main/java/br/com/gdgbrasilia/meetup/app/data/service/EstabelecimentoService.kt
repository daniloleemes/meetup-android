@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package br.com.gdgbrasilia.meetup.app.data.service

import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.business.vo.*
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.repository.EstabelecimentoRepository
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

class EstabelecimentoService {

    @Inject
    lateinit var estabelecimentoRepository: EstabelecimentoRepository

    @Inject
    lateinit var dataParser: DataParser

    init {
        RepositoryComponent.inject(this)
    }

    fun fetchTiposEstabelecimento(listener: (List<TipoEstabelecimento>?) -> Unit) {
        estabelecimentoRepository.fetchTiposEstabelecimento().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(dataParser.parseList(it.dados.lista, TipoEstabelecimento::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchTiposEstabelecimento(): Call<AppResponse> {
        return estabelecimentoRepository.fetchEstabelecimentos()
    }

    fun fetchEstabelecimentos(params: HashMap<String, String> = getDefaultParams(), listener: (List<Estabelecimento>?) -> Unit) {
        estabelecimentoRepository.fetchEstabelecimentos(params).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(dataParser.parseList(it.dados.lista, Estabelecimento::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchEstabelecimentos(params: HashMap<String, String> = getDefaultParams()): Call<AppResponse> {
        return estabelecimentoRepository.fetchEstabelecimentos(params)
    }

    fun fetchAvaliacoes(estabelecimentoID: String, listener: (List<Avaliacao>?) -> Unit) {
        val params = getDefaultParams()
        params["filtros"] = "estabelecimentoID.ToString().Equals(\"$estabelecimentoID\")"
        estabelecimentoRepository.fetchAvaliacoes(params).enqueue(parseResponse { appResponse, throwable ->
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

    fun consultarCEP(cep: String, listener: (Endereco?) -> Unit) {
        estabelecimentoRepository.consultarCEP(cep).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(it.endereco)
                } else {
                    listener(null)
                }
            }
        })
    }

    fun saveEstabelecimento(activity: AppCompatActivity, estabelecimento: EstabelecimentoDTO, fotos: List<LocalPictureDTO>, listener: (Boolean) -> Unit) {
        val fotosPart = fotos
                .filter { it.uri != null }
                .mapIndexed { index, fotoDTO ->
                    val file = FileUtils.instance.getFile(activity, fotoDTO.uri!!)
                    val requestFile = RequestBody.create(MediaType.parse(activity.contentResolver.getType(fotoDTO.uri!!)), file)
                    MultipartBody.Part.createFormData("fotos_$index", file!!.name, requestFile)
                }

        val estabelecimentoJson = Gson().toJson(estabelecimento)
        val estabelecimentoBody = RequestBody.create(MultipartBody.FORM, estabelecimentoJson)
        val estabelecimentoPart = MultipartBody.Part.createFormData("dados", null, estabelecimentoBody)

        estabelecimentoRepository.saveEstabelecimento(fotosPart, estabelecimentoPart, estabelecimento.endereco.latitude, estabelecimento.endereco.longitude).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let {
                listener(it.sucesso)
            }
        })
    }

    fun saveEstabelecimento(estabelecimento: Estabelecimento, listener: (Boolean) -> Unit) {
        estabelecimentoRepository.saveEstabelecimento(estabelecimento).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun marcarFavorito(estabelecimentoID: String, favorito: Boolean, listener: (Boolean) -> Unit) {
        estabelecimentoRepository.marcarFavorito(estabelecimentoID, favorito).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun marcarVisita(estabelecimentoID: String, visitou: Boolean, listener: (Boolean) -> Unit) {
        estabelecimentoRepository.marcarVisita(estabelecimentoID, visitou).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun denunciar(estabelecimentoID: String, tipoDenunciaID: String, denuncia: String, listener: (Boolean) -> Unit) {
        val params = HashMap<String, String>()
        params["estabelecimentoID"] = estabelecimentoID
        params["tipoDenunciaID"] = tipoDenunciaID
        params["descricao"] = denuncia

        estabelecimentoRepository.denunciar(params).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun avaliar(estabelecimentoID: String, avaliacao: Int, comentario: String, listener: (Boolean) -> Unit) {
        val params = HashMap<String, String>()
        params["estabelecimentoID"] = estabelecimentoID
        params["avaliacao"] = avaliacao.toString()
        params["comentario"] = comentario

        estabelecimentoRepository.avaliar(params).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun excluir(estabelecimentoID: String, listener: (Boolean) -> Unit) {
        estabelecimentoRepository.excluirEstabelecimento(estabelecimentoID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

}