package br.com.gdgbrasilia.meetup.app.data.repository

import br.com.gdgbrasilia.meetup.app.business.vo.AppResponse
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.EstabelecimentoDTO
import br.com.gdgbrasilia.meetup.app.util.extensions.getDefaultParams
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface EstabelecimentoRepository {

    @FormUrlEncoded
    @POST("TipoEstabelecimento/Listar")
    fun fetchTiposEstabelecimento(@FieldMap params: HashMap<String, String> = getDefaultParams()): Call<AppResponse>

    @FormUrlEncoded
    @POST("Estabelecimento/Listar")
    fun fetchEstabelecimentos(@FieldMap params: HashMap<String, String> = getDefaultParams()): Call<AppResponse>

    @POST("Estabelecimento/Salvar")
    fun saveEstabelecimento(@Body estabelecimento: EstabelecimentoDTO): Call<AppResponse>

    @POST("Estabelecimento/Excluir")
    fun excluirEstabelecimento(@Query("id") estabelecimentoID: String): Call<AppResponse>

    @Multipart
    @POST("Estabelecimento/Salvar")
    fun saveEstabelecimento(@Part files: List<MultipartBody.Part>, @Part estabelecimento: MultipartBody.Part, @Query("latitude") latitude: Double, @Query("longitude") longitude: Double): Call<AppResponse>

    @POST("Estabelecimento/Salvar")
    fun saveEstabelecimento(@Body estabelecimento: Estabelecimento): Call<AppResponse>

    @FormUrlEncoded
    @POST("EstabelecimentoAvaliacao/Listar")
    fun fetchAvaliacoes(@FieldMap params: HashMap<String, String>): Call<AppResponse>

    @POST("Usuarios/MarcarEstabelecimentoFavorito")
    fun marcarFavorito(@Query("estabelecimentoID") estabelecimentoID: String, @Query("favorito") favorito: Boolean): Call<AppResponse>

    @POST("EstabelecimentoAvaliacao/MarcarVisita")
    fun marcarVisita(@Query("estabelecimentoID") estabelecimentoID: String, @Query("visitou") visitou: Boolean): Call<AppResponse>

    @FormUrlEncoded
    @POST("EstabelecimentoDenuncia/Denunciar")
    fun denunciar(@FieldMap params: HashMap<String, String>): Call<AppResponse>

    @FormUrlEncoded
    @POST("EstabelecimentoAvaliacao/Avaliar")
    fun avaliar(@FieldMap params: HashMap<String, String>): Call<AppResponse>

    @FormUrlEncoded
    @POST("Utils/ConsultarCEP")
    fun consultarCEP(@Field("cep") cep: String): Call<AppResponse>

}