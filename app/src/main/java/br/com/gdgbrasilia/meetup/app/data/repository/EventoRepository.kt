package br.com.gdgbrasilia.meetup.app.data.repository

import br.com.gdgbrasilia.meetup.app.business.vo.AppResponse
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.business.vo.EventoDTO
import br.com.gdgbrasilia.meetup.app.util.extensions.getDefaultParams
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface EventoRepository {

    @FormUrlEncoded
    @POST("TipoEvento/Listar")
    fun fetchTiposEvento(@FieldMap params: HashMap<String, String> = getDefaultParams()): Call<AppResponse>

    @FormUrlEncoded
    @POST("Evento/Listar")
    fun fetchEventos(@FieldMap params: HashMap<String, String> = getDefaultParams()): Call<AppResponse>

    @POST("Evento/Salvar")
    fun saveEvento(@Body evento: EventoDTO): Call<AppResponse>

    @POST("Evento/Excluir")
    fun excluirEvento(@Query("id") eventoID: String): Call<AppResponse>

    @Multipart
    @POST("Evento/Salvar")
    fun saveEvento(@Part files: List<MultipartBody.Part>, @Part evento: MultipartBody.Part, @Query("latitude") latitude: Double, @Query("longitude") longitude: Double): Call<AppResponse>

    @POST("Evento/Salvar")
    fun saveEvento(@Body evento: Evento): Call<AppResponse>

    @FormUrlEncoded
    @POST("EventoAvaliacao/Listar")
    fun fetchAvaliacoes(@FieldMap params: HashMap<String, String>): Call<AppResponse>

    @FormUrlEncoded
    @POST("EventoDenuncia/Denunciar")
    fun denunciar(@FieldMap params: HashMap<String, String>): Call<AppResponse>

    @FormUrlEncoded
    @POST("EventoAvaliacao/Avaliar")
    fun avaliar(@FieldMap params: HashMap<String, String>): Call<AppResponse>

}