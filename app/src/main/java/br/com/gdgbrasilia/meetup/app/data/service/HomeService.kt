@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package mobi.happe.menugo.data.service

import android.annotation.SuppressLint
import android.content.SharedPreferences
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.vo.AppResponse
import br.com.gdgbrasilia.meetup.app.business.vo.Politica
import br.com.gdgbrasilia.meetup.app.business.vo.TipoDenuncia
import br.com.gdgbrasilia.meetup.app.business.vo.Usuario
import br.com.gdgbrasilia.meetup.app.data.AppApplication
import br.com.gdgbrasilia.meetup.app.data.AppApplication.Companion.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.repository.HomeRepository
import br.com.gdgbrasilia.meetup.app.data.util.DataParser
import br.com.gdgbrasilia.meetup.app.data.util.HttpUtils.Companion.parseLoginResponse
import br.com.gdgbrasilia.meetup.app.data.util.HttpUtils.Companion.parseResponse
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

/**
 * Created by danilolemes on 16/03/2018.
 */
class HomeService {

    @Inject
    lateinit var repository: HomeRepository

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var dataParser: DataParser

    init {
        RepositoryComponent.inject(this)
    }

    fun facebookLogin(token: String, listener: (Usuario?, mensagem: String?) -> Unit) {
        repository.loginFacebook(token).enqueue(parseLoginResponse { appResponse, throwable, cookie ->
            throwable?.let { listener(null, null); it.printStackTrace() }
            appResponse?.let { parseLoginResponse(appResponse, cookie, listener) }
        })
    }

    @SuppressLint("ApplySharedPref")
    fun saveCookie(cookie: String?) {
        if (!cookie.isNullOrEmpty()) {
            sharedPreferences
                    .edit()
                    .putString(AppApplication.instance.resources.getString(R.string.cookie), cookie)
                    .commit()
        }
    }

    @SuppressLint("ApplySharedPref")
    fun saveUserID(userID: String?) {
        if (!userID.isNullOrEmpty()) {
            sharedPreferences
                    .edit()
                    .putString(AppApplication.instance.resources.getString(R.string.usuario_id), userID)
                    .commit()
        }
    }

    fun fetchProfile(userID: String, listener: (Usuario?) -> Unit) {
        repository.fetchProfile(userID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(it.usuario)
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchTiposDenuncia(listener: (List<TipoDenuncia>?) -> Unit) {
        repository.fetchTiposDenuncia().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(dataParser.parseList(it.dados.lista, TipoDenuncia::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun fetchPoliticas(listener: (List<Politica>?) -> Unit) {
        repository.fetchPoliticas().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                if (it.sucesso) {
                    listener(dataParser.parseList(it.dados.lista, Politica::class.java))
                } else {
                    listener(null)
                }
            }
        })
    }

    fun excluirUsuario(usuarioID: String, listener: (Boolean) -> Unit) {
        repository.excluirUsuario(usuarioID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    fun salvarUsuario(usuario: Usuario, listener: (Boolean) -> Unit) {
        repository.salvarUsuario(usuario).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(false); it.printStackTrace() }
            appResponse?.let { listener(it.sucesso) }
        })
    }

    private fun saveDeviceToken(usuarioID: String) {
        val params = HashMap<String, String>()
        params["usuarioID"] = usuarioID
        params["token"] = FirebaseInstanceId.getInstance().token ?: ""
        params["tipo"] = "ANDROID"
        repository.salvarFcmToken(params).enqueue(parseResponse { appResponse, throwable ->

        })
    }

    private fun parseLoginResponse(appResponse: AppResponse, cookie: String?, listener: (Usuario?, mensagem: String?) -> Unit) {
        if (appResponse.sucesso) {
            saveCookie(cookie)
            saveUserID(appResponse.usuarioID)
            saveDeviceToken(appResponse.usuarioID)
            appResponse.usuario.usuarioID = appResponse.usuarioID
            listener(appResponse.usuario, null)
        } else {
            listener(null, appResponse.mensagem)
        }
    }

}