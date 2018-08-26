package br.com.gdgbrasilia.meetup.app.data.repository

import br.com.gdgbrasilia.meetup.app.business.vo.AppResponse
import br.com.gdgbrasilia.meetup.app.business.vo.Usuario
import br.com.gdgbrasilia.meetup.app.util.extensions.getDefaultParams
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by danilolemes on 20/12/2017.
 */
interface HomeRepository {

    @FormUrlEncoded
    @POST("Home/LoginFacebook")
    fun loginFacebook(@Field("token") token: String): Call<AppResponse>

    @POST("Usuarios/Excluir")
    fun excluirUsuario(@Query("id") usuarioID: String): Call<AppResponse>

    @POST("Usuarios/Salvar")
    fun salvarUsuario(@Body usuario: Usuario): Call<AppResponse>

    @FormUrlEncoded
    @POST("Home/LoginGoogle")
    fun loginGoogle(@Field("token") token: String): Call<AppResponse>

    @FormUrlEncoded
    @POST("Home/Login")
    fun loginEmail(@Field("email") email: String, @Field("senha") password: String, @Field("mobile") mobile: Boolean = true): Call<AppResponse>

    @POST("Home/Logout")
    fun logout(): Call<AppResponse>

    @FormUrlEncoded
    @POST("Home/Register")
    fun register(@Field("email") email: String, @Field("password") password: String, @Field("nome") nome: String, @Field("dataNascimento") dataNascimento: String = "", @Field("isMobile") isMobile: Boolean = true): Call<AppResponse>

    @FormUrlEncoded
    @POST("UsuarioToken/Salvar")
    fun salvarFcmToken(@FieldMap params: HashMap<String, String>): Call<AppResponse>

    @FormUrlEncoded
    @POST("Usuarios/Pagina")
    fun fetchProfile(@Field("id") usuarioID: String): Call<AppResponse>

    @FormUrlEncoded
    @POST("TipoDenuncia/Listar")
    fun fetchTiposDenuncia(@FieldMap params: HashMap<String, String> = getDefaultParams()): Call<AppResponse>

    @FormUrlEncoded
    @POST("Politica/Listar")
    fun fetchPoliticas(@FieldMap params: HashMap<String, String> = getDefaultParams()): Call<AppResponse>
}